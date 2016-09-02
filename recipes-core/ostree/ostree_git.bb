SUMMARY = "OSTree commits, downloads, and deploys bootable filesystem trees."
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=5f30f0716dfdd0d91eb439ebec522ec2"
DEPENDS = "gpgme glib-2.0 zlib e2fsprogs libcap libsoup-2.4"

inherit pkgconfig autotools systemd

PV = "0.7.0+gitr${SRCPV}"
SRCREV = "76166cb52eae8b2637503e5cac9743b283d429be"

SRC_URI = " \
    gitsm://github.com/ostreedev/ostree;protocol=https \
"

S = "${WORKDIR}/git"

do_configure_prepend() {
    pushd ${S}
    NOCONFIGURE=1 ./autogen.sh
    popd
}

SYSTEMD_SERVICE_${PN} = "ostree-prepare-root.service ostree-remount.service"

EXTRA_OECONF_class-target += "--enable-man=no --enable-rofiles-fuse=no"
DEPENDS_class-target += "gpgme glib-2.0 zlib xz e2fsprogs libsoup-2.4"
FILES_${PN} += "/usr/share/gir-1.0 /usr/lib/girepository-1.0"

# Fuse is in meta-openembedded, and does not support native builds by
# default -- uncomment only if fuse-based optimization needed.
# DEPENDS_class-native += "fuse"
# EXTRA_OECONF_class-native += "--enable-man=no --enable-rofiles-fuse=yes"

EXTRA_OECONF_class-native += "--enable-man=no --enable-rofiles-fuse=no"

BBCLASSEXTEND = "native"
