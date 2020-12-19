import random
import json
import urllib.request
import subprocess
import os

output = ""

operators = ["+", "-", "*", "/"] # add more operators here
functions = ["abs", "sqrt", "max", "min"] # add more functions here

left_paren = 0
right_paren = 0

for _ in range(3): # How many rounds
    for i in range(1, 1000): # Length of generated input
        if i % 2 == 0:
            output += random.choice(operators)
        else:
            if random.random() < 0.1: # 10% chance that it will generate a function
                function = random.choice(functions)
                output += function
                output += "("
                output += str(random.randint(1, 9))
                if function == "max" or function == "min":
                    for i in range(1, random.randint(1,15)):
                        output += ("," + str(random.randint(-100, 100)))
                output += ")"
            elif random.random() < 0.5: # 50% chance that it will create a parenthesis
                paren = random.choice(["(", ")"])
                if paren == ")" and left_paren > right_paren:
                    if not output[-1].isdigit():
                        output += str(random.randint(1, 25))
                    output += ")"
                    right_paren = right_paren + 1
                else:
                    output += "("
                    left_paren = left_paren + 1
                    output += str(random.randint(1, 25))
            else:
                output += str(random.randint(1, 25))

    while left_paren > right_paren:
        output += ")"
        right_paren = right_paren + 1

    body = {
        "expr": output
    }

    print("Evaluating")
    print("")
    print(output)
    print(output, file=open('tmp.txt', 'a'))

    req = urllib.request.Request("http://api.mathjs.org/v4/", json.dumps(body).encode("utf-8"), headers={'content-type': 'application/json'})

    response = urllib.request.urlopen(req)

    print("CalcMalc") 
    subprocess.call(['java', '-jar', 'app-1.0.0.jar', 'tmp.txt'])
    print('Mathjs')
    print(json.loads(response.read().decode('utf-8'))['result'])
    print("")
    os.remove('tmp.txt')
    output = ""
