cmake_minimum_required(VERSION 3.10)
project(LC3_disassembler_C C)

set(CMAKE_C_STANDARD 11)

include_directories(.)

add_executable(LC3_disassembler_C
        disasm.c)
