LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}/:"
SRC_URI_append = " \
    file://ostree \
    file://0001-Improve-tests-for-presence-of-rootfs.patch \
    file://wipe_efi_boot_targets.sh \
    file://init_efi_boot_targets.sh \
"

PACKAGES_append = " initramfs-module-ostree "

do_install_append() {
    # ostree
    install -m 0755 ${WORKDIR}/ostree ${D}/init.d/95-ostree

    install -d -m 0755 ${D}/usr/bin
    install -m 0755 ${WORKDIR}/wipe_efi_boot_targets.sh ${D}/usr/bin/wipe_efi_boot_targets.sh
    install -m 0755 ${WORKDIR}/init_efi_boot_targets.sh ${D}/usr/bin/init_efi_boot_targets.sh
}

SUMMARY_initramfs-module-ostree = "initramfs support for ostree based pivotrouting"
RDEPENDS_initramfs-module-ostree = "${PN}-base"
FILES_initramfs-module-ostree = " \
  /init.d/95-ostree \
  /usr/bin/wipe_efi_boot_targets.sh \
  /usr/bin/init_efi_boot_targets.sh \
"
