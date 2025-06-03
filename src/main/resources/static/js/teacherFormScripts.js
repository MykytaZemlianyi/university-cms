const selectedCourseIds = new Set();

document.querySelectorAll('#selectedCoursesHiddenInputs input[name="courses"]').forEach(input => {
    selectedCourseIds.add(input.value);
});


document.addEventListener('change', function(e) {
    if (e.target.name === 'courses') {
        const id = e.target.value;
        if (e.target.checked) {
            selectedCourseIds.add(id);
        } else {
            selectedCourseIds.delete(id);
        }
    }
});

document.getElementById('teacherForm').addEventListener('submit', function() {
    updateSelectedCourseHiddenInputs();
    console.log('Courses to submit:', Array.from(selectedCourseIds));
});

function saveCurrentlyCheckedCourses() {
    document.querySelectorAll('input[name="courses"]:checked').forEach(cb => {
        selectedCourseIds.add(cb.value);
    });
}

function updateSelectedCourseHiddenInputs() {
    const courseContainer = document.getElementById('selectedCoursesHiddenInputs');
    if (!courseContainer) {
        console.error('Element with id "selectedCoursesHiddenInputs" not found');
        return;
    }
    courseContainer.innerHTML = '';
    selectedCourseIds.forEach(id => {
        const input = document.createElement('input');
        input.type = 'hidden';
        input.name = 'courses';
        input.value = id;
        courseContainer.appendChild(input);
    });
}

function loadCourses(page, size = 5) {
    saveCurrentlyCheckedCourses();

    const form = document.getElementById('teacherForm');
    const formData = new FormData(form);

    formData.append('currentPage', page);
    formData.append('size', size);
    selectedCourseIds.forEach(id => formData.append('selectedCourseIds', id));

    fetch('/courses/courseSelectCheckboxList', {
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
            document.getElementById('courses-list').innerHTML = html;

            document.querySelectorAll('input[name="courses"]').forEach(cb => {
                cb.checked = selectedCourseIds.has(cb.value);
            });

            updateSelectedCourseHiddenInputs();
        })
        .catch(error => {
            console.error('Error loading courses:', error);
        });
}

