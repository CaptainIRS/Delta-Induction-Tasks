function [w] = KeyExpansion()
    K = Config.K;
    Nk = Config.Nk;
    Nb = Config.Nb;
    Nr = Config.Nr;
    warning('off');
    w = gf(zeros(Nb*(Nr+1), 4), 8, Config.mx);
    
    for i = 1:Nk
       j = 4*(i-1)+1;
       w(i,:) = [K(j) K(j+1) K(j+2) K(j+3)];
%        t = w(i,:);
%        fprintf("\ni = %d, wi   = ", i);
%        fprintf("%0x ", t.x);
    end
    for i = Nk+1:Nb*(Nr+1)
         temp = w(i-1, :);
%         fprintf("\ni = %d, temp = ", i);
%         fprintf("%0x ", temp.x);
        if mod(i-1, Nk) == 0
%             fprintf("\ni = %d, rotw = ", i);
%             fprintf("%0x ", RotWord(temp).x);
%             t = SubWord(RotWord(temp));
%             fprintf("\ni = %d, srtw = ", i);
%             fprintf("%0x ", t.x);
            temp = SubWord(RotWord(temp)) + Rcon((i-1)/Nk);
        elseif Nk > 6 && mod(i-1, Nk) == 4
%             fprintf("\ni = %d, subw = ", i);
%             fprintf("%0x ", SubWord(temp).x);
            temp = SubWord(temp);
        end
        w(i, :) = w(i-Nk, :) + temp;
    end
    w = int32(w.x);
end