#include <stdlib.h>
#include <stdio.h>

/* Opcode definitions */
enum opcodes_t { BR, ADD, LD, ST, JSR, AND, LDR, STR, RTI, NOT, LDI, STI, JMP, MUL, LEA, TRAP };

int get_zext_field(int bits, int hi_bit, int lo_bit)
{
  // Check for reversed operands
  if (lo_bit > hi_bit) {
    return get_zext_field(bits, lo_bit, hi_bit);
  }

  bits = ~(~0 << (hi_bit+1)) & bits;  // Mask off high-order bits
  bits = bits >> lo_bit;  // Shift away low-order bits
  return bits;
}

int get_sext_field(int bits, int hi_bit, int lo_bit)
{
  // Check for reversed operands
  if (lo_bit > hi_bit) {
    return get_sext_field(bits, lo_bit, hi_bit);
  }
  
  int most_significant_bit = bits & (1 << hi_bit);
  if (most_significant_bit != 0) {
    bits = (~0 << hi_bit) | bits;  // Sign extend
  } else {
    bits = ~(~0 << (hi_bit+1)) & bits;  // Zero extend
  }

  bits = bits >> lo_bit;  // Shift away low-order bits
  return bits;
}

int get_bit(int bits, int bit_number)
{
  return get_zext_field(bits, bit_number, bit_number);
}

void print_fill(int ir)
{
  printf(".FILL x%04X", ir);
}

void print_instruction(int pc, int ir)
{
  int opcode = get_zext_field(ir, 15, 12);
  switch (opcode) {
  case ADD:                                        /* ADD */
    {
      int dr = get_zext_field(ir, 11, 9);
      int sr1 = get_zext_field(ir, 8, 6);
      if (get_bit(ir, 5) == 0) {
        if (get_zext_field(ir, 4, 3) == 0) {
          int sr2 = get_zext_field(ir, 2, 0);
          printf("ADD R%d, R%d, R%d", dr, sr1, sr2); 
        } else {
          print_fill(ir);
        }
      } else {
        int imm5 = get_sext_field(ir, 4, 0);
        printf("ADD R%d, R%d, x%x", dr, sr1, imm5);
      }
    }
    break;
  case AND:                                        /* AND */
    {

      // Your code here

    }
    break;
  case BR:                                         /* BR */
    {

      // Your code here

    }
    break;
  case JMP:                                        /* JMP/RET */
    {

      // Your code here

    }
    break;
  case JSR:                                        /* JSR/JSRR */
    {

      // Your code here

    }
    break;
  case LD:                                          /* LD */
    {

      // Your code here

    }
    break;
  case ST:                                         /* ST */
    {

      // Your code here

    }
    break;
  case LDI:                                        /* LDI */
    {

      // Your code here

    }
    break;
  case STI:                                        /* STI */
    {

      // Your code here

    }
    break;
  case LDR:                                        /* LDR */
    {

      // Your code here

    }
    break;
  case STR:                                        /* STR */
    {

      // Your code here

    }
    break;
  case LEA:                                        /* LEA */
    {

      // Your code here

    }
    break;
  case NOT:                                        /* NOT */
    {

      // Your code here

    }
    break;
  case RTI:                                        /* RTI */
    {

      // Your code here

    }
    break;
  case TRAP:                                       /* TRAP */
    {

      // Your code here

    }
    break;
  case MUL:                                        /* MUL */
    {

      // Your code here

    }
    break;
  }
  // you may find the folloving helpful in debugging
  // printf("\t\t\t; x%04X\n", ir);
}

int get_word_from_file(FILE* f)
{
  int byte1 = fgetc(f);
  int byte2 = fgetc(f);
  if ((byte1 == EOF) || (byte2 == EOF)) {
    return -1;
  } 
  byte1 = byte1 & 0xff;  // Ignore all but lowest 8 bits
  byte2 = byte2 & 0xff;  // Ignore all but lowest 8 bits
  return (byte1 << 8) | byte2; // Combine bytes
}

int main(int argc, char **argv) 
{
  FILE* f;
  int ir;
  int pc;

  if (argc != 2) {
    printf("Usage: %s <obj-file>\n", argv[0]);
    return 1;
  }

  f = fopen(argv[1], "r");
  if (f == NULL) {
    printf("Error opening file %s\n", argv[1]);
    return 1;
  }

  pc = get_word_from_file(f);
  printf(".orig x%04X\n", pc);

  // Call print_instruction for each 16-bit word in the file
  ir = get_word_from_file(f);
  while (ir != -1) {
    print_instruction(pc, ir);
    ir = get_word_from_file(f);
    pc++;
  }
  printf(".end\n");
  return 0;
}
