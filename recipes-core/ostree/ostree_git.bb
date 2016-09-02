SUMMARY = "OSTree commits, downloads, and deploys bootable filesystem trees."
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=5f30f0716dfdd0d91eb439ebec522ec2"
DEPENDS = "gpgme glib-2.0 zlib xz e2fsprogs libcap libsoup-2.4 gobject-introspection"

inherit pkgconfig autotools systemd

PV = "2016.9+gitr${SRCPV}"
SRCREV = "2aacc6912b78e7d3158f072cd9c1b3c5d0bddc22"

SRC_URI = " \
    gitsm://github.com/ostreedev/ostree;protocol=https \
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

EXTRA_OECONF_class-target += "--enable-man=no --enable-rofiles-fuse=no"
DEPENDS_class-target += "gpgme glib-2.0 zlib xz e2fsprogs libcap libsoup-2.4 gobject-introspection"

# Fuse is in meta-openembedded, and does not support native builds by
# default -- uncomment only if fuse-based optimization needed.
# DEPENDS_class-native += ${DEPENDS}
# DEPENDS_class-native += "fuse"
# EXTRA_OECONF_class-native += "--enable-man=no --enable-rofiles-fuse=yes"

EXTRA_OECONF_class-native += "--enable-man=no --enable-rofiles-fuse=no"

FILES_${PN} += "/usr/share/gir-1.0 /usr/lib/girepository-1.0"

BBCLASSEXTEND = "native"
