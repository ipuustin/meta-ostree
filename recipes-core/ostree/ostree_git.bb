SUMMARY = "OSTree commits, downloads, and deploys bootable filesystem trees."
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=5f30f0716dfdd0d91eb439ebec522ec2"
DEPENDS = "gpgme glib-2.0 zlib xz e2fsprogs libsoup-2.4 gobject-introspection ca-certificates"

inherit pkgconfig autotools systemd

PV = "2017.1+gitr${SRCPV}"
SRCREV = "6517a8a27a1386e7cb5482e7cb2919fe92721ccf"

SRC_URI = " \
    gitsm://github.com/ostreedev/ostree;protocol=https \
    file://0001-build-allow-controlling-gobject-introspection-data-g.patch \
    file://0001-libostree-fix-missing-macros-when-compiling-without-.patch \
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

EXTRA_OECONF_class-target += " \
  --disable-man \
  --disable-rofiles-fuse \
  --disable-otmpfile \
  --without-libarchive \
  --disable-gobject-introspection \
"

DEPENDS_class-target += " \
    gpgme glib-2.0 zlib xz e2fsprogs libsoup-2.4 \
    gobject-introspection efivar \
"

RDEPENDS_${PN}_class-target += "gnupg efibootmgr lshw"
RDEPENDS_${PN}_class-native += "gnupg"

# Fuse is in meta-openembedded, and does not support native builds by
# default -- uncomment only if fuse-based optimization needed.
# DEPENDS_class-native += ${DEPENDS}
# DEPENDS_class-native += "fuse"
# EXTRA_OECONF_class-native += "--enable-man=no --enable-rofiles-fuse=yes"

EXTRA_OECONF_class-native += " \
  --disable-man \
  --disable-rofiles-fuse \
  --disable-otmpfile \
  --without-libarchive \
  --disable-gobject-introspection \
  --with-builtin-grub2-mkconfig \
"



FILES_${PN} += "/usr/share/gir-1.0 /usr/lib/girepository-1.0"
BBCLASSEXTEND = "native"
