	.orig x3000
	;; Objective: Take the value from R2, triple it, and put it in R2
	;; INSTRUCTIONS
	;; Store a counter in R0
	;; Keep a temporary multiplied value in R1
	;; Store the result in R2
START
	AND R0, R0, #0 ;; Initialize R0 to 0
	ADD R0, R0, #3 ;; Set R0 (which should now be ZERO) to 3
	AND R1, R1, #0 ;; Initialize R1 (our temporary storage) to 0
	LOOP
		ADD R1, R1, R2 ;; Add R1 (temp var) and R2 (the number we're multiplying) and store them in R1 (temp var)
		ADD R0, R0, #-1 ;; Decrement our counter by 1
		BRp LOOP ;; If our counter isn't yet zero (it is positive), we should add again
	AND R2, R2, #0 ;; Prepare R2 to receive the new value by initializing it
	ADD R2, R2, R1 ;; Add the values from R2 (which should be ZERO) and from R1 (our temp var) and store in R2
		HALT
		.end
