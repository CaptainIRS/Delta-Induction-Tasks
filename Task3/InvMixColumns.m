function [state] = InvMixColumns(state)
    row = [0x0e 0x0b 0x0d 0x09];
    A = gf(gallery('circul', row), 8, Config.mx);
    state = A * state;
    state = state.x;
end