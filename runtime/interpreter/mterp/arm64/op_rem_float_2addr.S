    /* rem vA, vB */
    lsr     w3, wINST, #12              // w3<- B
    ubfx    w9, wINST, #8, #4           // w9<- A
    GET_VREG s1, w3
    GET_VREG s0, w9
    bl  fmodf
    ubfx    w9, wINST, #8, #4           // w9<- A
    FETCH_ADVANCE_INST 1                // advance rPC, load rINST
    GET_INST_OPCODE ip                  // extract opcode from rINST
    SET_VREG s0, w9
    GOTO_OPCODE ip                      // jump to next instruction
