function adder1(base) {
    base = Number(base);
    for (let j = 1; <error>j < arguments</error>; j++) {
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

let res1 = <error>height - x</error>;
let res2 = height - y;
