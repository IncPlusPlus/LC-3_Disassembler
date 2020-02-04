        .ORIG x30F6
START   ;; 
        ;; YOUR CODE GOES HERE!!!
        ;; 
		LEA R1, #-3
		ADD R2, R1, #14
		ST R2, #-5
		AND R2, R2, #0
		ADD R2, R2, #5
		STR R2, R1, #14
		LDI R3, #-9
                
        HALT
        .END
        