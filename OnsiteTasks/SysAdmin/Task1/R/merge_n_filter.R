file1 <- read.csv("file1.txt")
file2 <- read.csv("file2.txt")
file3 <- read.csv("file3.txt")

merge12 <- merge(file1, file2, by = 'roll', all = TRUE, incomparables = NA)
merge123 <- merge(merge12, file3, by = 'email', all = TRUE, incomparables = NA)

write.csv(merge123, 'merged.txt', row.names = FALSE)

matched <- readline(prompt = "Enter input: ")
splitString <- strsplit(matched, ": ")
colName <- splitString[[1]][1]
pattern <- paste0('^', splitString[[1]][2])

print(merge123[grep(pattern, merge123[[colName]]), ])