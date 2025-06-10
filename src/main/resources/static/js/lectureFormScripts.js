let selectedCourseId = null;
let selectedRoomId = null;

const courseHiddenInput = document.getElementById('selectedCourseId');
if (courseHiddenInput) {
    selectedCourseId = courseHiddenInput.value || null;
}
const roomHiddenInput = document.getElementById('selectedRoomId');
if (roomHiddenInput) {
    selectedRoomId = roomHiddenInput.value || null;
}

document.addEventListener('change', function(e) {
    if (e.target.name === 'courseRadio') {
        selectedCourseId = e.target.value || null;
        updateSelectedCourseHiddenInput();
    }
    if (e.target.name === 'roomRadio') {
        selectedRoomId = e.target.value || null;
        updateSelectedRoomHiddenInput();
    }
});

function updateSelectedCourseHiddenInput() {
    const input = document.getElementById('selectedCourseId');
    if (input) {
        input.value = selectedCourseId || '';
    } else {
        console.error('Could not find the hidden input for selectedCourseId');
    }
}
function updateSelectedRoomHiddenInput() {
    const input = document.getElementById('selectedRoomId');
    if (input) {
        input.value = selectedRoomId || '';
    } else {
        console.error('Could not find the hidden input for selectedRoomId');
    }
}

function loadCourses(page, size = 5) {
    const form = document.getElementById('lectureForm');
    const formData = new FormData(form);

    formData.append('page', page);
    formData.append('size', size);
    if (selectedCourseId) {
        formData.append('selectedCourseId', selectedCourseId);
    }

    fetch('/courses/courseSelectRadioList', {
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
            const container = document.getElementById('courseRadioContainer');
            if (container) {
                container.innerHTML = html;

                if (selectedCourseId !== null) {
                    const selectedRadio = document.querySelector(`input[name="courseRadio"][value="${selectedCourseId}"]`);
                    if (selectedRadio) {
                        selectedRadio.checked = true;
                    }
                }
                updateSelectedCourseHiddenInput();
            } else {
                console.error('Could not find the hidden input for selectedCourseId');
            }
        })
        .catch(error => {
            console.error('Network response was not ok: ', error);
        });
}
function loadRooms(page, size = 5) {
    const form = document.getElementById('roomForm');
    const formData = new FormData(form);

    formData.append('page', page);
    formData.append('size', size);
    if (selectedRoomId) {
        formData.append('selectedRoomId', selectedRoomId);
    }

    fetch('/rooms/roomSelectRadioList', {
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
            const container = document.getElementById('roomRadioContainer');
            if (container) {
                container.innerHTML = html;

                if (selectedRoomId !== null) {
                    const selectedRadio = document.querySelector(`input[name="roomRadio"][value="${selectedRoomId}"]`);
                    if (selectedRadio) {
                        selectedRadio.checked = true;
                    }
                }
                updateSelectedRoomHiddenInput();
            } else {
                console.error('Could not find the hidden input for selectedRoomId');
            }
        })
        .catch(error => {
            console.error('Network response was not ok: ', error);
        });
}