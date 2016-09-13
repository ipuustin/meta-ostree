# Class for creating the ostree repo associated to a build

DEPENDS="ostree-native"
OSTREE_REPO="${TMPDIR}/ostree-repo"

do_ostree_repo() {
    # Create an empty repo, if one is not already available
    if [ ! -d "$OSTREE_REPO" ]; then
        mkdir "${OSTREE_REPO}"
#        ostree --repo="${OSTREE_REPO}" init --mode=archive-z2
    fi
}

#ROOTFS_POSTPROCESS_COMMAND += "do_ostree_repo;"
