	.orig x3000
	;; Objective: Store a logical OR operation
	;; R5 = R2 || R3
	;; DeMorgan's Law states that !(P || Q) == !P && !Q
	;; INSTRUCTIONS
	;; Store !R2 in R0 and store !R3 in R1
	;; Store R0 && R1 in R4
	;; Store !R4 in R5
START
		NOT R0, R2
		NOT R1, R3
		AND R4, R0, R1
		NOT R5, R4
		HALT
		.end
