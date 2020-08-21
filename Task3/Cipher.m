function [out] =  Cipher(in)
    Nb = Config.Nb;
    Nr = Config.Nr;

    in = reshape(in, Nb, 4);
    state = int32(in);
    % fprintf("%0x ", state);
    % fprintf("\n");
    w = KeyExpansion();
    state = AddRoundKey(state, w(1:Nb, :)');
    % fprintf("Round Key:\n");
    % fprintf("%0x", w(1:Nb, :));
    % fprintf("State:\n");
    % fprintf("%0x ", state);
    % fprintf("\n");
    
    for round = 1:(Nr-1)
        state = SubBytes(state);
        state = ShiftRows(state);
        state = MixColumns(state);
        state = AddRoundKey(state, w(round*Nb+1:(round+1)*Nb, :)');
        % fprintf("%0x ", state);
        % fprintf("\n");
    end
    state = SubBytes(state);
    state = ShiftRows(state);
    state = AddRoundKey(state, w(Nr*Nb+1:(Nr+1)*Nb, :)');
    % fprintf("%0x ", state);
    % fprintf("\n");
    out = state;
end
