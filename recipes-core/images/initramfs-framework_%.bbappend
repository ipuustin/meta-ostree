LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}/:"
SRC_URI_append = " file://ostree"

do_install_append() {
    # ostree
    install -m 0755 ${WORKDIR}/e2fs ${D}/init.d/95-ostree
}

SUMMARY_initramfs-module-ostree = "initramfs support for ostree based pivotrouting"
RDEPENDS_initramfs-module-ostree = "${PN}-base"
FILES_initramfs-module-ostree = "/init.d/95-ostree"
