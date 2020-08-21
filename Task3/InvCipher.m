function [out] = InvCipher(in)
    Nb = Config.Nb;
    Nr = Config.Nr;
    
    w = KeyExpansion();
    
    state = int32(in);
    state = AddRoundKey(state, w(Nr*Nb+1:(Nr+1)*Nb, :));
    
    for round = (Nr-1):-1:1
       state = InvShiftRows(state);
       state = InvSubBytes(state);
       state = AddRoundKey(state, w(round*Nb+1:(round+1)*Nb, :)); 
       state = InvMixColumns(state);
    end
    
    state = InvShiftRows(state);
    state = InvSubBytes(state);
    state = AddRoundKey(state, w(1:Nb, :));
    
    out = state;
end
