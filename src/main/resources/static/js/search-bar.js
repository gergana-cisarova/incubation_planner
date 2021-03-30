const projectList = document.getElementById('projectList');
const searchBar = document.getElementById('searchInput');
const allBtn = document.getElementById('all');
const myProjectBtn = document.getElementById('own');
const collabsBtn = document.getElementById('collabs');
const allProjects = [];
fetch("http://localhost:8080/manageProjects/api")
    .then(response => response.json())
    .then(data => {
        for (let d of data) {
            allProjects.push(d);
        }
    });

const myProjects = [];
fetch("http://localhost:8080/manageProjects/own")
    .then(response => response.json())
    .then(data => {
        for (let d of data) {
            myProjects.push(d);
        }
    });

const myCollabProjects = [];
fetch("http://localhost:8080/manageProjects/collaborations")
    .then(response => response.json())
    .then(data => {
        for (let d of data) {
            myCollabProjects.push(d);
        }
    });

// Start of functions

allBtn.addEventListener('click', (e) => {
    projectList.innerHTML = ""
    displayProjects(allProjects);

})

searchBar.addEventListener('keyup', (e) => {
    const searchingCharacters = searchBar.value.toLowerCase();
    let filteredProjects = allProjects.filter(p => {
        return p.name.toLowerCase().includes(searchingCharacters)
            || p.sector.toLowerCase().includes(searchingCharacters);
    });
    projectList.innerHTML = ""
    displayProjects(filteredProjects);
})

myProjectBtn.addEventListener('click', (e) => {
    projectList.innerHTML = ""
    displayProjects(myProjects);
})

collabsBtn.addEventListener('click', (e) => {
    projectList.innerHTML = ""
    displayProjects(myCollabProjects);
})

function displayProjects(projects) {
    projectList.innerHTML = projects
        .map((p) => {
            return `    <tr>
                <td >${p.name}</td>
                <td >${p.sector}</td>
                <td>${p.startDate}</td>
                <td>
                    <a href="/projects/details/${p.id}">
                        <button type="button">Details</button></a>
             
                </td>
            </tr> `
        })
        .join('');

}
