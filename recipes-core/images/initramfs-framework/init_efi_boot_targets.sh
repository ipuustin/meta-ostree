#½/bin/sh
efibootmgr -c -d /dev/mmcblk0 -p 2 -L "Ostro XT Internal 2" -l "\EFI\BOOT\bootx64.efi"
efibootmgr -c -d /dev/mmcblk0 -p 1 -L "Ostro XT Internal 1" -l "\EFI\BOOT\bootx64.efi"
efibootmgr -c -d /dev/sda -p 2 -L "Ostro XT USB 2" -l "\EFI\BOOT\bootx64.efi"
efibootmgr -c -d /dev/sda -p 1 -L "Ostro XT USB 1" -l "\EFI\BOOT\bootx64.efi"

