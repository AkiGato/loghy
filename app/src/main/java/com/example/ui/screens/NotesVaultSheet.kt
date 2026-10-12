package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NoteVersionSnapshot
import com.example.data.model.VaultNote
import com.example.ui.components.FluidBulletDroplet
import com.example.ui.components.NeumorphicButton
import com.example.ui.components.NeumorphicDebossedBox
import com.example.ui.components.NeumorphicRaisedBox
import com.example.ui.components.PaperPalettePicker
import com.example.ui.components.UndulatingInkUnderline
import com.example.ui.theme.FaintInk
import com.example.ui.theme.GhostInk
import com.example.ui.theme.InkBlack
import com.example.ui.theme.MutedInk
import com.example.ui.theme.NotePaperTones
import com.example.ui.theme.PaperBase
import com.example.ui.theme.PaperCard
import com.example.ui.theme.PaperSurface
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NotesVaultScreen(
    notes: List<VaultNote>,
    editingNote: VaultNote?,
    snapshots: List<NoteVersionSnapshot>,
    isEditing: Boolean,
    initialShowSnapshots: Boolean = false,
    onOpenNote: (VaultNote?) -> Unit,
    onOpenNoteHistory: (VaultNote) -> Unit = {},
    onCloseEditor: () -> Unit,
    onSaveNote: (title: String, content: String, paperTone: Int, tags: List<String>) -> Unit,
    onRollback: (NoteVersionSnapshot) -> Unit,
    onDeleteNote: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    if (isEditing) {
        NoteEditorView(
            note = editingNote,
            snapshots = snapshots,
            initialShowSnapshots = initialShowSnapshots,
            onClose = onCloseEditor,
            onSave = onSaveNote,
            onRollback = onRollback,
            onDelete = { editingNote?.let { onDeleteNote(it.id) } },
            modifier = modifier
        )
    } else {
        NotesListView(
            notes = notes,
            onAddNote = { onOpenNote(null) },
            onSelectNote = onOpenNote,
            onOpenHistory = onOpenNoteHistory,
            modifier = modifier
        )
    }
}

@Composable
private fun NotesListView(
    notes: List<VaultNote>,
    onAddNote: () -> Unit,
    onSelectNote: (VaultNote) -> Unit,
    onOpenHistory: (VaultNote) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PaperBase)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "OFFLINE VAULT",
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    ),
                    color = InkBlack
                )
                Text(
                    text = "Edit Notes Anytime · Full Version Edit History",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                    color = GhostInk
                )
            }

            NeumorphicButton(
                onClick = onAddNote,
                modifier = Modifier
                    .size(44.dp)
                    .testTag("add_note_button"),
                shape = RoundedCornerShape(22.dp),
                contentPadding = 0.dp
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "New Note",
                    tint = InkBlack,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        UndulatingInkUnderline(
            modifier = Modifier.padding(vertical = 8.dp),
            heightDp = 4.dp
        )

        if (notes.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                NeumorphicRaisedBox(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    backgroundColor = PaperSurface
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        FluidBulletDroplet(sizeDp = 16.dp)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Notes Vault is ready.",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold
                            ),
                            color = InkBlack
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Capture 2 AM sparks, takeaways from SWOT, or tap '+' to write. All notes can be edited with complete version history.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MutedInk,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        NeumorphicButton(
                            onClick = onAddNote,
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    tint = InkBlack,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Write New Note",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = InkBlack
                                )
                            }
                        }
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(notes, key = { it.id }) { note ->
                    val toneColor = NotePaperTones.getOrElse(note.paperTone) { PaperSurface }
                    val dateFormat = SimpleDateFormat("MMM d, yyyy · h:mm a", Locale.getDefault())

                    NeumorphicRaisedBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectNote(note) }
                            .testTag("vault_note_${note.id}"),
                        shape = RoundedCornerShape(16.dp),
                        backgroundColor = toneColor,
                        elevation = 3.5.dp
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = note.title.ifBlank { "Untitled Reflection" },
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontFamily = FontFamily.Serif,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = InkBlack,
                                    modifier = Modifier.weight(1f)
                                )
                                FluidBulletDroplet(sizeDp = 8.dp)
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = note.content.ifBlank { "(No content yet)" },
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontFamily = FontFamily.Serif,
                                    lineHeight = 20.sp
                                ),
                                color = GhostInk,
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Timestamp and Tags
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = dateFormat.format(Date(note.updatedAt)),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MutedInk
                                )

                                if (note.tags.isNotEmpty()) {
                                    Text(
                                        text = note.tags.joinToString(" · "),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = InkBlack,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Action buttons: Edit Note & Edit History
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                NeumorphicButton(
                                    onClick = { onOpenHistory(note) },
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = 6.dp
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.History,
                                            contentDescription = "Edit History",
                                            tint = InkBlack,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Edit History",
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                            color = InkBlack
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                NeumorphicButton(
                                    onClick = { onSelectNote(note) },
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = 6.dp
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit Note",
                                            tint = InkBlack,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Edit",
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = InkBlack
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NoteEditorView(
    note: VaultNote?,
    snapshots: List<NoteVersionSnapshot>,
    initialShowSnapshots: Boolean = false,
    onClose: () -> Unit,
    onSave: (String, String, Int, List<String>) -> Unit,
    onRollback: (NoteVersionSnapshot) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var title by remember(note?.id) { mutableStateOf(note?.title ?: "") }
    var content by remember(note?.id) { mutableStateOf(note?.content ?: "") }
    var paperTone by remember(note?.id) { mutableIntStateOf(note?.paperTone ?: 0) }
    var showSnapshots by remember(note?.id, initialShowSnapshots) { mutableStateOf(initialShowSnapshots) }
    var previewingSnapshotId by remember { mutableStateOf<Long?>(null) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    val currentBg = NotePaperTones.getOrElse(paperTone) { PaperSurface }

    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = {
                Text(
                    text = "Delete Note?",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = InkBlack
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to delete \"${title.ifBlank { "Untitled Reflection" }}\"? This will remove all revision snapshots permanently.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = GhostInk
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteConfirmation = false
                        onDelete()
                    }
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmation = false }) {
                    Text("Cancel", color = InkBlack)
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PaperBase)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Top Navigation Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NeumorphicButton(
                onClick = onClose,
                modifier = Modifier
                    .size(44.dp)
                    .testTag("note_editor_back"),
                shape = RoundedCornerShape(22.dp),
                contentPadding = 0.dp
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = InkBlack,
                    modifier = Modifier.size(20.dp)
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (note != null) {
                    // Edit History Toggle Button with revision badge
                    NeumorphicButton(
                        onClick = { showSnapshots = !showSnapshots },
                        modifier = Modifier.testTag("note_history_button"),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = 8.dp
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = "Version History",
                                tint = if (showSnapshots) MaterialTheme.colorScheme.primary else InkBlack,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (snapshots.isNotEmpty()) "History (${snapshots.size})" else "History",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (showSnapshots) MaterialTheme.colorScheme.primary else InkBlack
                            )
                        }
                    }

                    // Delete Note Button
                    NeumorphicButton(
                        onClick = { showDeleteConfirmation = true },
                        modifier = Modifier
                            .size(44.dp)
                            .testTag("delete_note_button"),
                        shape = RoundedCornerShape(22.dp),
                        contentPadding = 0.dp
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // Save Note Button
                NeumorphicButton(
                    onClick = {
                        onSave(title, content, paperTone, note?.tags ?: listOf("Reflection"))
                    },
                    modifier = Modifier.testTag("save_note_button"),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = 8.dp
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = "Save",
                            tint = InkBlack,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Save",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = InkBlack
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Tactile Paper Palette Selector
        PaperPalettePicker(
            selectedToneIndex = paperTone,
            onToneSelected = { paperTone = it }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Version History & Rollback Panel
        AnimatedVisibility(visible = showSnapshots) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                NeumorphicDebossedBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 120.dp, max = 220.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    if (snapshots.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Initial version active. Edit and save to record snapshots.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MutedInk
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            item {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 4.dp, vertical = 2.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "EDIT HISTORY (${snapshots.size} VERSIONS)",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 1.sp
                                        ),
                                        color = GhostInk
                                    )
                                    Text(
                                        text = "Tap Restore to roll back",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MutedInk
                                    )
                                }
                            }

                            itemsIndexed(snapshots, key = { _, snap -> snap.id }) { index, snap ->
                                val dateFormat = SimpleDateFormat("MMM d, h:mm:ss a", Locale.getDefault())
                                val isLatest = index == 0
                                val isExpanded = previewingSnapshotId == snap.id

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(PaperSurface)
                                        .clickable {
                                            previewingSnapshotId = if (isExpanded) null else snap.id
                                        }
                                        .padding(horizontal = 10.dp, vertical = 8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = "Version ${snap.versionNumber}",
                                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                    color = InkBlack
                                                )
                                                if (isLatest) {
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    Text(
                                                        text = "Current",
                                                        style = MaterialTheme.typography.labelSmall.copy(
                                                            fontSize = 10.sp,
                                                            fontWeight = FontWeight.Bold
                                                        ),
                                                        color = MaterialTheme.colorScheme.primary
                                                    )
                                                }
                                            }
                                            Text(
                                                text = dateFormat.format(Date(snap.snapshotTimestamp)),
                                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                                color = MutedInk
                                            )
                                        }

                                        NeumorphicButton(
                                            onClick = {
                                                title = snap.title
                                                content = snap.content
                                                onRollback(snap)
                                            },
                                            modifier = Modifier.height(28.dp),
                                            shape = RoundedCornerShape(6.dp),
                                            contentPadding = 4.dp
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.Default.Restore,
                                                    contentDescription = "Restore",
                                                    tint = InkBlack,
                                                    modifier = Modifier.size(13.dp)
                                                )
                                                Spacer(modifier = Modifier.width(3.dp))
                                                Text(
                                                    text = "Restore",
                                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                    color = InkBlack
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    // Content snippet / expanded text
                                    Text(
                                        text = snap.content.ifBlank { "(Empty note body)" },
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontFamily = FontFamily.Serif,
                                            lineHeight = 16.sp
                                        ),
                                        color = GhostInk,
                                        maxLines = if (isExpanded) 10 else 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Note Content Sheet (Neumorphic Card)
        NeumorphicRaisedBox(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            shape = RoundedCornerShape(18.dp),
            backgroundColor = currentBg,
            elevation = 5.dp
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                // Title Field
                BasicTextField(
                    value = title,
                    onValueChange = { title = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("note_title_input"),
                    textStyle = MaterialTheme.typography.headlineMedium.copy(
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        color = InkBlack
                    ),
                    cursorBrush = SolidColor(InkBlack),
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        if (title.isEmpty()) {
                            Text(
                                text = "Note Title...",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontFamily = FontFamily.Serif,
                                    color = MutedInk
                                )
                            )
                        }
                        innerTextField()
                    }
                )

                UndulatingInkUnderline(
                    modifier = Modifier.padding(vertical = 8.dp),
                    heightDp = 3.dp
                )

                // Body Content Field
                BasicTextField(
                    value = content,
                    onValueChange = { content = it },
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("note_content_input"),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        fontFamily = FontFamily.Serif,
                        lineHeight = 24.sp,
                        color = InkBlack
                    ),
                    cursorBrush = SolidColor(InkBlack),
                    decorationBox = { innerTextField ->
                        if (content.isEmpty()) {
                            Text(
                                text = "Write deep reflections, Socratic takeaways, or lateral ideas...",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontFamily = FontFamily.Serif,
                                    color = MutedInk
                                )
                            )
                        }
                        innerTextField()
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
