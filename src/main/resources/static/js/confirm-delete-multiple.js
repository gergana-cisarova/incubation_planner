const deleteBtns = Array.from(document.getElementsByClassName('bg-danger'))

deleteBtns.forEach(b => b.addEventListener('click', (e) => {
    let confirmation = confirm("The action will delete all ideas submitted by this user and all projects created from these ideas. Proceed anyway?")
    if (confirmation === false) {
        e.preventDefault();
    }
}))

