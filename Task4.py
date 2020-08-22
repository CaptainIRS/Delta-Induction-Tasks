from sys import argv
from threading import Thread

if __name__ == "__main__":
    '''
    Iterates through all possible values of X and Y in `wIhEXtAhKcXY` less
    than the specified range
    
    Usage: python Task4.py X Y
    Arguments: X and Y ranges for substitution
    '''
    if len(argv) != 3:
        print('Enter exactly 2 arguments')
    else:
        for x in range(int(argv[1])):
            for y in range(int(argv[2])):
                Thread(
                    target=print(f'Trying: wIhE{x}tAhKc{x}{y}'),
                    args=[x, y]
                ).start()

