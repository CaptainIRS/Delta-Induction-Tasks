# File 1 columns: "name","email","roll""
# File 2 columns: "roll", "branch"
# File 3 columns: "email", "phone", "grad"

# Merge file 1 and file 2 by roll number
awk '
	BEGIN{
		FS=",";
		print $0
	} 
	FNR==NR{
		a[$3]=$0;
		next;
	}{
		print a[$1] FS $2 > "merged12.txt"
	} 
' file1.txt file2.txt 

# Merge the above file and file 3 by email
awk '
	BEGIN{
		FS=",";
		print $0
	} 
	FNR==NR{
		a[$2]=$0;
		next;
	}{
		print a[$1] FS $3 > "merged.txt"
	}
' merged12.txt file3.txt

# Remove temporary file
rm merged12.txt
