package az.kodcraft.client.data.service

import az.kodcraft.client.data.dto.ClientDto
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldPath
import kotlinx.coroutines.tasks.await

class ClientService(
    private val usersRef: CollectionReference,
    private val clientsRef: CollectionReference
) {
    suspend fun getClients(trainerId: String, searchText: String): List<ClientDto> {
        // Step 1: Get the list of client documents for the given trainerId
        val clientDocs = clientsRef
            .whereEqualTo("trainer_id", trainerId)
            .get()
            .await()
            .documents

        // Step 2: Get the trainee ids from the client documents
        val traineeIds = clientDocs.mapNotNull { it.getString("trainee_id") }

        // Step 3: Get the user documents for the trainee ids
        val userDocs = usersRef
            .whereIn(FieldPath.documentId(), traineeIds)
            .orderBy("name")
            .startAt(searchText)
            .endAt(searchText + "\uf8ff")
            .get()
            .await()


        // Step 4: Map the user documents to ClientDto
        return userDocs.documents.mapNotNull { userDoc ->
            val traineeId = userDoc.id
            val name = userDoc.getString("name")
            if (name != null) {
                ClientDto(id = userDoc.id, name = name, traineeId = traineeId)
            } else {
                null
            }
        }
    }
}