library(stringi)

capitalLetters <- function(n, length) {
    stri_rand_strings(n, length, '[A-U]')
}

smallLetters <- function(n, length) {
    stri_rand_strings(n, length, '[a-u]')
}

randomNumbers <- function(n, digits) {
    as.numeric(
        paste0(sample((10 ** (digits - 1)):(10 ** digits - 1), n))
    )
}

randomNames <- function(n) {
    paste0(capitalLetters(n, 1), smallLetters(n, 5), ' ', 
           capitalLetters(n, 1), smallLetters(n, 7))
}

randomEmails <- function(n) {
    paste0(smallLetters(n, 7), '@', 
           smallLetters(n, 5), '.',
           smallLetters(n, 3))
}

randomRollNos <- function(n) {
    as.numeric(
        paste0(11, randomNumbers(n, 5))
    )
}

randomBranch <- function(n) {
    replicate(n, sample(c('CSE', 'ICE', 'MME', 'ECE', 'EEE'), 1))
}

randomYears <- function(n) {
    replicate(n, sample(2016:2025, 1))
}

name = randomNames(200)
email = randomEmails(200)
roll = randomRollNos(200)
branch = randomBranch(150)
phone = randomNumbers(100, digits = 10)
grad = randomYears(100)


file1 <- data.frame(name, email, roll)
write.csv(file1, "file1.txt", row.names = FALSE)

reducedRoll1 <- sample(roll, 150)
file2 <- data.frame(
    roll = reducedRoll1, 
    branch
)
write.csv(file2, "file2.txt", row.names = FALSE)

reducedRoll2 <- sample(file2$roll, 100)
file3 <- data.frame(
    email = subset(file1, roll %in% reducedRoll2)$email,
    phone,
    grad = randomYears(100)
)
write.csv(file3, "file3.txt", row.names = FALSE)