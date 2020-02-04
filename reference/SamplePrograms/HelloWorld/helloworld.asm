; Print Hello World on the console
 .ORIG x3000
 LEA R0, Hi
 PUTS
 HALT
Hi .STRINGZ "Hello World"
 .END