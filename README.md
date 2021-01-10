# Photo Rename

This is a simple program to rename my holiday pictures to a pattern that:
 - improves the alphabetical order
 - includes the day number, the place (optional) and a description (optional)
 
 The meta information are provided by a spread sheet (e.g. Excel, Calc) exported as CSV.
 
## Example:
| File Name          | Day | Place | Description       |
|--------------------|-----|-------|-------------------|
| DSC0008.jpg        | 1   |  Hwy5 | Sunset            |
| 20200101_123.jpeg  | 1   |  Hwy5 | NiceRedBridge     |
| DSC0100.jpg        | 2   |       | ViewFromHotelRoom |

* 0001-01-Hwy5-Sunset-DSC0008.jpg
* 0002-01-Hwy5-NiceRedBridge-20200101_123.jpg
* 0003-02-ViewFromHotelRoom-20200101_123.jpg

## Notes
* You can0t use the dash (`-`) in the original file name, palace and description,
  because it's the separator in new file name.
* After you renamed everything, you can change the table and rerun the programm.
  The original file name is conserved as _identifier_, so the renaming algorithm
  is idempotent and repeatable. 