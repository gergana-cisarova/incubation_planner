const deleteBtn = document.getElementById('delete')
deleteBtn.addEventListener('click', (e) => {
let confirmation = confirm("Please confirm delete")
    if (confirmation === false) {
        e.preventDefault();
    }
})

