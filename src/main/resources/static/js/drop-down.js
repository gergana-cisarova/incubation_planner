let labFData = {
    "Wood workshop": ["Leonardo"],
    "Metal workshop": ["Tesla"],
    "Digital production workshop": ["Lumiere"],
    "Prototyping space": ["Bell"],
    "Computers, Multimedia, Printers": ["Monnet", "Ideation", "STEM&Art", "Carnegie"]
}

window.onload = function () {

    let equipmentSelect = document.getElementById("inputNeededEquipment")
    let labSelect = document.getElementById("inputLab")

    for (let x in labFData) {
        equipmentSelect.options[equipmentSelect.options.length] = new Option(x, x)
    }
    equipmentSelect.onchange = function () {
        labSelect.options.length = 0
        let allLabs = labFData[this.value]

        for (let y in allLabs) {

            labSelect.options[labSelect.options.length] = new Option(allLabs[y], allLabs[y])


        }
    }
}
