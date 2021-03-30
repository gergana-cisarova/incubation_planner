const deleteBtn = document.getElementById('assign')
deleteBtn.addEventListener("click", handler, false)

function handler(e) {
        let confirmationChange = confirm("This action will override the current roles of the selected user. Proceed anyway?")
    if (confirmationChange === false) {
        e.preventDefault();
    }
}

