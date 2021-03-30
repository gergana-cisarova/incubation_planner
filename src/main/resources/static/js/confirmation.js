const message = document.getElementById('confirm');
message.style.display="none"
if(message.innerText !== "") {
    setTimeout(function () {
           message.style.display='block'},
        500);
    setTimeout(function () {
            message.style.display='none'},
        10000);
}

