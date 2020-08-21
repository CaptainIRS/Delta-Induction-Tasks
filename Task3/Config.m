classdef Config
    properties (Constant)
        Nb = 4,  % number of 32 bit words in the State
        Nk = 4,  % number of 32 bit words in the Cipher Key
        Nr = 10, % number of rounds
        K = [0x2b 0x7e 0x15 0x16 0x28 0xae 0xd2 0xa6 0xab 0xf7 0x15 0x88 0x09 0xcf 0x4f 0x3c];
        sbox = TestSBox(),
        invsbox = InvSBox(),
        mx = hex2dec('11B') % irreducible polynomial for GF(2^8) => [1 0 0 0 1 1 0 1 1]
    end
end
