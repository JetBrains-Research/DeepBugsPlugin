let x = 1;
let y = 2;
function setSize(xx, yy) {
    x = xx;
    y = yy;
}

let width = 5;
let height = 10;
setSize(width, height);
<error>setSize(height, width)</error>;


let promise = new Promise((resolve, reject) => {
    if (Math.random() * 100 < 90) {
    resolve('Success');
}
reject(new Error('Error'));
});

let onSuccess = (resolvedValue) => console.log(resolvedValue);
let onError = (error) => console.log(error);

<error>promise.then(onError, onSuccess)</error>;
promise.then(onSuccess, onError);
