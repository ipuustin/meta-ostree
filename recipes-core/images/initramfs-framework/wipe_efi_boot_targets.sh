#½/bin/sh

# This script will remove the first 100 boot entries
# from the table in the EFI BIOS

for partition in `seq 0 1 9`; do
  for device in `seq 0 1 9`; do
    efibootmgr -B -b $device$partition
  done
done
output=$(/usr/sbin/efibootmgr 2>&1)
echo $output
if [ $? -ne 0 ]; then
    echo "Error."
    exit -1
fi

