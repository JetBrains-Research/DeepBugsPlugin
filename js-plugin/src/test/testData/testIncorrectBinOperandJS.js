function adder1(base) {
    base = Number(base);
    for (let j = 1; <warning descr="Possibly incorrect operand: “j” or “arguments”">j < arguments</warning>; j++) {
    base += Number(arguments[j]);
}
return base;
}

function adder2(base) {
    base = Number(base);
    for (let j = 1; j < arguments.length; j++) {
        base += Number(arguments[j]);
    }
    return base;
}


let x = 1;
let y = 2;
let height = 3;

let res1 = <warning descr="Possibly incorrect operand: “height” or “x”">height - x</warning>;
let res2 = height - y;
