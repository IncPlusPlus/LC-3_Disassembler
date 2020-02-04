	.orig x3000
	;; Objective: Take the value from R3 and fill memory starting from x4000 R2 times
	;; INSTRUCTIONS
	;; We'll let let R2 act as our counter that we will decrement
	;; We'll have R0 be a variable that keeps the current address of the array
	;;
START
	LD R0, STARTPOS ;; Keep the index we're filling in R0

	LOOP
		STR R3, R0, #0 ;; Put the value of R3 into address R0 with offset 0
		ADD R0, R0, #1 ;; Increment the address in memory that R0 is pointing at
		ADD R2, R2, #-1 ;; Decrement our counter by 1
		BRp LOOP ;; If our counter isn't yet zero (it is positive), we should keep filling
		HALT
	STARTPOS .FILL x4000
		.end
