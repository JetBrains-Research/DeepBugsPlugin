let x = 1;
let y = 2;
function setSize(xx, yy) {
    x = xx;
    y = yy;
}

let width = 5;
let height = 10;
setSize(width, height);
<warning descr="Possibly wrong order of function arguments">setSize(height, width)</warning>;


let promise = new Promise((resolve, reject) => {
    if (Math.random() * 100 < 90) {
    resolve('Success');
}
reject(new Error('Error'));
});

let onSuccess = (resolvedValue) => console.log(resolvedValue);
let onError = (error) => console.log(error);

<warning descr="Possibly wrong order of function arguments">promise.then(onError, onSuccess)</warning>;
promise.then(onSuccess, onError);
