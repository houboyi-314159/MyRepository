extern _MessageBoxA@16
extern _ExitProcess@4

section .data
    msg db 'Hello, NASM!', 0
    title db 'Hello from NASM', 0

section .text
    global _main

_main:
    ; 调用 MessageBoxA
    push 0
    push title
    push msg  
    push 0
    call _MessageBoxA@16

    ; 调用 ExitProcess
    push 0
    call _ExitProcess@4