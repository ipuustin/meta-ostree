SUMMARY = "OSTree commits, downloads, and deploys bootable filesystem trees."
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=5f30f0716dfdd0d91eb439ebec522ec2"
DEPENDS = "gpgme glib-2.0 zlib xz e2fsprogs libsoup-2.4 gobject-introspection efivar ca-certificates"

inherit pkgconfig autotools systemd

PV = "2016.12+gitr${SRCPV}"
SRCREV = "24ac4ff190a96de47fab73d81c257f089c0e2020"

SRC_URI = " \
    gitsm://github.com/ostreedev/ostree;protocol=https \
    file://0001-Added-EFI-boot-loader-support-skeleton.patch \
    file://0002-Use-autoconf-to-make-efivar-optional.patch \
    file://0003-Added-efi-change-boot.patch \
    file://0004-Update-to-libefivar-27.patch \
"

S = "${WORKDIR}/git"

do_configure_prepend() {
    rm -f ${S}/gtk-doc.make
    cat > ${S}/gtk-doc.make <<EOF
EXTRA_DIST =
CLEANFILES =
EOF

    sed -e 's,$(libglnx_srcpath),libglnx,g' < ${S}/libglnx/Makefile-libglnx.am > ${S}/libglnx/Makefile-libglnx.am.inc
    sed -e 's,$(libbsdiff_srcpath),bsdiff,g' < ${S}/bsdiff/Makefile-bsdiff.am > ${S}/bsdiff/Makefile-bsdiff.am.inc
}

SYSTEMD_SERVICE_${PN} = "ostree-prepare-root.service ostree-remount.service"

EXTRA_OECONF_class-target += "--enable-man=no --enable-rofiles-fuse=no --with-efiblob=yes"
DEPENDS_class-target += "gpgme glib-2.0 zlib xz e2fsprogs libsoup-2.4 gobject-introspection efivar"

# Fuse is in meta-openembedded, and does not support native builds by
# default -- uncomment only if fuse-based optimization needed.
# DEPENDS_class-native += ${DEPENDS}
# DEPENDS_class-native += "fuse"
# EXTRA_OECONF_class-native += "--enable-man=no --enable-rofiles-fuse=yes"

EXTRA_OECONF_class-native += "--enable-man=no --enable-rofiles-fuse=no --enable-otmpfile=no"
EXTRA_OECONF_class-native += "--enable-man=no --enable-rofiles-fuse=no --enable-otmpfile=no --with-efiblob=no"

FILES_${PN} += "/usr/share/gir-1.0 /usr/lib/girepository-1.0"

BBCLASSEXTEND = "native"
