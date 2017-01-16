# Class for creating the ostree repo associated to a build

DEPENDS="ostree-native"
OSTREE_REPO="${TMPDIR}/ostree-repo"

OSTREE_DIR="${IMAGE_ROOTFS}/../"
OSTREE_SYSROOT_DIR="${OSTREE_DIR}/sysroot/"
OSTREE_ARCHIVE_REPO="${OSTREE_DIR}/ostree_archive_repo"
OSTREE_BARE_REPO="${OSTREE_SYSROOT_DIR}/ostree/repo"
BRANCH_NAME="${DISTRO}/${MACHINE}/standard"
OS_NAME="${DISTRO}"
REMOTE="${DISTRO}"

do_ostree_repo() {
    # hacks
    mkdir -p "${IMAGE_ROOTFS}/usr/lib/ostree/"
    mkdir -p "${IMAGE_ROOTFS}/boot/"
    mkdir -p "${IMAGE_ROOTFS}/sysroot"
    echo "NOTHING_TO_SEE_HERE" > "${IMAGE_ROOTFS}/boot/vmlinuz-6286b5d95fe100a61ad576564d2d74085a66d28245f5d1037ec45780b8287522"
    echo "ID=${DISTRO}" > "${IMAGE_ROOTFS}/etc/os-release"
    rm -rf "${OSTREE_SYSROOT_DIR}"

    # create the necessary structure for postinst tasks
    mkdir -p "${OSTREE_SYSROOT_DIR}"
    mkdir -p "${OSTREE_SYSROOT_DIR}/etc/"
    mkdir -p "${OSTREE_SYSROOT_DIR}/var/lib/opkg"
    touch "${OSTREE_SYSROOT_DIR}/var/lib/opkg/status"

    # copy /var to a distro-global directory, later bind mounted to deployments
    mv "${IMAGE_ROOTFS}/var" "${OSTREE_DIR}/backup-var"
    mkdir -p "${IMAGE_ROOTFS}/var"

    #real recipe
    ostree admin os-init --sysroot="${OSTREE_SYSROOT_DIR}" "${OS_NAME}" 
    ostree --repo="${OSTREE_ARCHIVE_REPO}" init --mode=archive-z2
    ostree --repo="${OSTREE_ARCHIVE_REPO}" commit -s 'build' -b "${BRANCH_NAME}" --tree=dir="${IMAGE_ROOTFS}"

    LOCAL_OSTREE_CHECKSUM=`ostree --repo="${OSTREE_ARCHIVE_REPO}" rev-parse "${BRANCH_NAME}"`
    echo "NOTHING_TO_SEE_HERE" > "${IMAGE_ROOTFS}/boot/vmlinuz-$LOCAL_OSTREE_CHECKSUM"

    ostree --repo="${OSTREE_BARE_REPO}" pull-local --untrusted "${OSTREE_ARCHIVE_REPO}" --remote="${REMOTE}" "${BRANCH_NAME}"
    ostree admin deploy --sysroot="${OSTREE_SYSROOT_DIR}" --os="${OS_NAME}" "${REMOTE}:${BRANCH_NAME}"

    # put the distro-global /var in place
    rm -rf "${OSTREE_SYSROOT_DIR}/ostree/deploy/${OS_NAME}/var/"
    mv "${OSTREE_DIR}/backup-var" "${OSTREE_SYSROOT_DIR}/ostree/deploy/${OS_NAME}/var"

    # hack(ish)
    cp -a "${IMAGE_ROOTFS}/boot" "${OSTREE_SYSROOT_DIR}"
    rm -rf "${IMAGE_ROOTFS}"

    ln -s `realpath -sm "${OSTREE_SYSROOT_DIR}"` "${IMAGE_ROOTFS}"

    # IMAGE_ROOTFS="${OSTREE_SYSROOT_DIR}/ostree/deploy/${OS_NAME}/deploy/$LOCAL_OSTREE_CHECKSUM.0/"
}

ROOTFS_POSTPROCESS_COMMAND_append = " do_ostree_repo;"
