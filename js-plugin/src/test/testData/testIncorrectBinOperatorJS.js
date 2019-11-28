function adder1(base) {
    base = Number(base);
    for (let i = 1; <error>i <= arguments.length</error>; i++) {
    base += Number(arguments[i]);
}
return base;
}

function adder2(base) {
    base = Number(base);
    for (let i = 1; i < arguments.length; i++) {
        base += Number(arguments[i]);
    }
    return base;
}


let text = 1;
let str = 2;

<error>text / str</error>;
text + str;
