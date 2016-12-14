#½/bin/sh
efibootmgr -c -d /dev/sda -p 2 -L "USB 2" -l "\EFI\BOOT\bootx64.efi"
efibootmgr -c -d /dev/sda -p 1 -L "USB 1" -l "\EFI\BOOT\bootx64.efi"

