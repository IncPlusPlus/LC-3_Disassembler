# cmake_minimum_required(VERSION <specify CMake version here>)
cmake_minimum_required(VERSION 3.10)

project(C_Dissasem C)

set(CMAKE_C_STANDARD 11)

include_directories(.)

add_executable(C_Dissasem
        lc3dis.c)
