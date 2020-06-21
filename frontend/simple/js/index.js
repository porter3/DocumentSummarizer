$(document).ready(function () {

    const uploadUrl = 'http://localhost:8080/upload'

    $('#uploadButton').click(() => {
        const form = new FormData($('#uploadForm')[0])

        $.ajax({
            type: "POST",
            url: uploadUrl,
            data: form,
            enctype: 'multipart/form-data',
            processData: false,
            contentType: false,
            success: response => {
                alert('Successful upload');
            },
            error: xhr => {
                alert(xhr.responseText);
            }
        })
    })

})