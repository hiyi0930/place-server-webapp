package com.lsy

import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor
import org.springframework.restdocs.operation.preprocess.Preprocessors.*


fun getDocumentResponse(): OperationResponsePreprocessor {
    return preprocessResponse(prettyPrint())
}