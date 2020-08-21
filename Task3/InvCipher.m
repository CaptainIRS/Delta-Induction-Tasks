function [out] = InvCipher(in)
    Nb = Config.Nb;
    Nr = Config.Nr;
    
    w = KeyExpansion();
    
    in = reshape(in, Nb, 4);
    state = int32(in);
    fprintf("First Round: \n");
    fprintf("%0x ", state);
    fprintf("\n");
    state = AddRoundKey(state, w(Nr*Nb+1:(Nr+1)*Nb, :)');
    fprintf("FirstRoundKey: ");
    fprintf("%0x ", w(Nr*Nb+1:(Nr+1)*Nb, :)');
    fprintf("\nState:");
    fprintf("%0x ", state);
    
    for round = (Nr-1):-1:1
       fprintf("\n\nRound %d: ", round);
       fprintf("\nInvShiftRows  : ");
       state = InvShiftRows(state);
       fprintf("%0x ", state);
       fprintf("\nInvSubBytes   : ");
       state = InvSubBytes(state);
       fprintf("%0x ", state);
       fprintf("\nRoundKey      : ");
       fprintf("%0x ", w(round*Nb+1:(round+1)*Nb, :));
       fprintf("\nAddRoundKey   : ");
       state = AddRoundKey(state, w(round*Nb+1:(round+1)*Nb, :)');
       fprintf("%0x ", state);
       fprintf("\nInvMixColumns : ");
       state = InvMixColumns(state);
       fprintf("%0x ", state);
    end
    
    fprintf("\nInvShiftRows : ");
    state = InvShiftRows(state);
    fprintf("%0x ", state);
    fprintf("\nInvSubBytes  : ");
    state = InvSubBytes(state);
    fprintf("\nRoundKey     : ");
    fprintf("%0x ", w(1:Nb, :)');
    fprintf("%0x ", state);
    fprintf("\nAddRoundKey  : ");
    state = AddRoundKey(state, w(1:Nb, :)');
    fprintf("%0x ", state);
    fprintf("\n");
    
    out = state;
end