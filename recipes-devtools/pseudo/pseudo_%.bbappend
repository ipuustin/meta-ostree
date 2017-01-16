FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_append = " \
    file://0001-ports-linux-guts-special-case-fcntl-F_OFD_-to-get-ri.patch \
"

