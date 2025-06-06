let selectedTeacherId = null;

const teacherHiddenInput = document.getElementById('selectedTeacherId');
if (teacherHiddenInput) {
    selectedTeacherId = teacherHiddenInput.value || null;
}

document.addEventListener('change', function(e) {
    if (e.target.name === 'teacherRadio') {
        selectedTeacherId = e.target.value || null;
        updateSelectedTeacherHiddenInput();
    }
});

function updateSelectedTeacherHiddenInput() {
    const input = document.getElementById('selectedTeacherId');
    if (input) {
        input.value = selectedTeacherId || '';
    } else {
        console.error('Could not find the hidden input for selectedTeacherId');
    }
}

function loadTeachers(page, size = 5) {
    const form = document.getElementById('courseForm');
    const formData = new FormData(form);

    formData.append('page', page);
    formData.append('size', size);
    if (selectedTeacherId) {
        formData.append('selectedTeacherId', selectedTeacherId);
    }

    fetch('/teachers/teacherSelectRadioList', {
        method: 'POST',
        body: formData
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok: ' + response.statusText);
            }
            return response.text();
        })
        .then(html => {
            const container = document.getElementById('teacherRadioContainer');
            if (container) {
                container.innerHTML = html;

                if (selectedTeacherId !== null) {
                    const selectedRadio = document.querySelector(`input[name="teacherRadio"][value="${selectedTeacherId}"]`);
                    if (selectedRadio) {
                        selectedRadio.checked = true;
                    }
                }
                updateSelectedTeacherHiddenInput();
            } else {
                console.error('Could not find the hidden input for selectedTeacherId');
            }
        })
        .catch(error => {
            console.error('Network response was not ok: ', error);
        });
}