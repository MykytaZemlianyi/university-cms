let selectedGroupId = null;

const groupHiddenInput = document.getElementById('selectedGroupId');
if (groupHiddenInput) {
    selectedGroupId = groupHiddenInput.value || null;
}

document.addEventListener('change', function(e) {
    if (e.target.name === 'groupRadio') {
        selectedGroupId = e.target.value || null;
        updateSelectedGroupHiddenInput();
    }
});

function updateSelectedGroupHiddenInput() {
    const input = document.getElementById('selectedGroupId');
    if (input) {
        input.value = selectedGroupId || '';
    } else {
        console.error('Could not find the hidden input for selectedGroupId');
    }
}

function loadGroups(page, size = 5) {
    const form = document.getElementById('studentForm');
    const formData = new FormData(form);

    formData.append('page', page);
    formData.append('size', size);
    if (selectedGroupId) {
        formData.append('selectedGroupId', selectedGroupId);
    }

    fetch('/groups/groupSelectRadioList', {
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
            const container = document.getElementById('groupRadioContainer');
            if (container) {
                container.innerHTML = html;

                if (selectedGroupId !== null) {
                    const selectedRadio = document.querySelector(`input[name="groupRadio"][value="${selectedGroupId}"]`);
                    if (selectedRadio) {
                        selectedRadio.checked = true;
                    }
                }
                updateSelectedGroupHiddenInput();
            } else {
                console.error('Could not find the hidden input for selectedGroupId');
            }
        })
        .catch(error => {
            console.error('Network response was not ok: ', error);
        });
}