	.orig x3000
	;; Objective: Take the value from R2, multiply it by R3, and put it in R5
	;; INSTRUCTIONS
	;; We'll let let R3 act as our counter that we will decrement
	;; Keep a temporary multiplied value in R5
	;; Store the result in R5
START
	AND R5, R5, #0 ;; Initialize R5 (the destination) to 0
	;; No need to initialize or set a counter here as R3 servers that purpose
	LOOP
		ADD R5, R2, R5 ;; Add R1 (temp var) and R2 (the number we're multiplying) and store them in R5 (destination)
		ADD R3, R3, #-1 ;; Decrement our counter by 1
		BRp LOOP ;; If our counter isn't yet zero (it is positive), we should add again
		HALT
		.end
