package com.example.agronote.data.local;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.agronote.data.model.PlantNote;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class PlantNoteDao_Impl implements PlantNoteDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PlantNote> __insertionAdapterOfPlantNote;

  private final EntityDeletionOrUpdateAdapter<PlantNote> __updateAdapterOfPlantNote;

  private final SharedSQLiteStatement __preparedStmtOfSoftDeleteNote;

  private final SharedSQLiteStatement __preparedStmtOfHardDeleteNote;

  public PlantNoteDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPlantNote = new EntityInsertionAdapter<PlantNote>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `plant_notes` (`id`,`plantName`,`conditionDetails`,`observationDate`,`status`,`localPhotoPath`,`cloudPhotoUrl`,`isSynced`,`isDeleted`,`updatedAt`,`userId`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PlantNote entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getPlantName());
        statement.bindString(3, entity.getConditionDetails());
        statement.bindLong(4, entity.getObservationDate());
        statement.bindString(5, entity.getStatus());
        if (entity.getLocalPhotoPath() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getLocalPhotoPath());
        }
        if (entity.getCloudPhotoUrl() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getCloudPhotoUrl());
        }
        final int _tmp = entity.isSynced() ? 1 : 0;
        statement.bindLong(8, _tmp);
        final int _tmp_1 = entity.isDeleted() ? 1 : 0;
        statement.bindLong(9, _tmp_1);
        statement.bindLong(10, entity.getUpdatedAt());
        statement.bindString(11, entity.getUserId());
      }
    };
    this.__updateAdapterOfPlantNote = new EntityDeletionOrUpdateAdapter<PlantNote>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `plant_notes` SET `id` = ?,`plantName` = ?,`conditionDetails` = ?,`observationDate` = ?,`status` = ?,`localPhotoPath` = ?,`cloudPhotoUrl` = ?,`isSynced` = ?,`isDeleted` = ?,`updatedAt` = ?,`userId` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PlantNote entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getPlantName());
        statement.bindString(3, entity.getConditionDetails());
        statement.bindLong(4, entity.getObservationDate());
        statement.bindString(5, entity.getStatus());
        if (entity.getLocalPhotoPath() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getLocalPhotoPath());
        }
        if (entity.getCloudPhotoUrl() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getCloudPhotoUrl());
        }
        final int _tmp = entity.isSynced() ? 1 : 0;
        statement.bindLong(8, _tmp);
        final int _tmp_1 = entity.isDeleted() ? 1 : 0;
        statement.bindLong(9, _tmp_1);
        statement.bindLong(10, entity.getUpdatedAt());
        statement.bindString(11, entity.getUserId());
        statement.bindString(12, entity.getId());
      }
    };
    this.__preparedStmtOfSoftDeleteNote = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE plant_notes SET isDeleted = 1, isSynced = 0, updatedAt = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfHardDeleteNote = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM plant_notes WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertNote(final PlantNote note, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPlantNote.insert(note);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAll(final List<PlantNote> notes,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPlantNote.insert(notes);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateNote(final PlantNote note, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfPlantNote.handle(note);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object softDeleteNote(final String id, final long timestamp,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSoftDeleteNote.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, timestamp);
        _argIndex = 2;
        _stmt.bindString(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfSoftDeleteNote.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object hardDeleteNote(final String id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfHardDeleteNote.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfHardDeleteNote.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<PlantNote>> getAllNotes() {
    final String _sql = "SELECT * FROM plant_notes WHERE isDeleted = 0 ORDER BY observationDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"plant_notes"}, new Callable<List<PlantNote>>() {
      @Override
      @NonNull
      public List<PlantNote> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPlantName = CursorUtil.getColumnIndexOrThrow(_cursor, "plantName");
          final int _cursorIndexOfConditionDetails = CursorUtil.getColumnIndexOrThrow(_cursor, "conditionDetails");
          final int _cursorIndexOfObservationDate = CursorUtil.getColumnIndexOrThrow(_cursor, "observationDate");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfLocalPhotoPath = CursorUtil.getColumnIndexOrThrow(_cursor, "localPhotoPath");
          final int _cursorIndexOfCloudPhotoUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "cloudPhotoUrl");
          final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isDeleted");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final List<PlantNote> _result = new ArrayList<PlantNote>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PlantNote _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpPlantName;
            _tmpPlantName = _cursor.getString(_cursorIndexOfPlantName);
            final String _tmpConditionDetails;
            _tmpConditionDetails = _cursor.getString(_cursorIndexOfConditionDetails);
            final long _tmpObservationDate;
            _tmpObservationDate = _cursor.getLong(_cursorIndexOfObservationDate);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpLocalPhotoPath;
            if (_cursor.isNull(_cursorIndexOfLocalPhotoPath)) {
              _tmpLocalPhotoPath = null;
            } else {
              _tmpLocalPhotoPath = _cursor.getString(_cursorIndexOfLocalPhotoPath);
            }
            final String _tmpCloudPhotoUrl;
            if (_cursor.isNull(_cursorIndexOfCloudPhotoUrl)) {
              _tmpCloudPhotoUrl = null;
            } else {
              _tmpCloudPhotoUrl = _cursor.getString(_cursorIndexOfCloudPhotoUrl);
            }
            final boolean _tmpIsSynced;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSynced);
            _tmpIsSynced = _tmp != 0;
            final boolean _tmpIsDeleted;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsDeleted);
            _tmpIsDeleted = _tmp_1 != 0;
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final String _tmpUserId;
            _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            _item = new PlantNote(_tmpId,_tmpPlantName,_tmpConditionDetails,_tmpObservationDate,_tmpStatus,_tmpLocalPhotoPath,_tmpCloudPhotoUrl,_tmpIsSynced,_tmpIsDeleted,_tmpUpdatedAt,_tmpUserId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getNoteById(final String id, final Continuation<? super PlantNote> $completion) {
    final String _sql = "SELECT * FROM plant_notes WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<PlantNote>() {
      @Override
      @Nullable
      public PlantNote call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPlantName = CursorUtil.getColumnIndexOrThrow(_cursor, "plantName");
          final int _cursorIndexOfConditionDetails = CursorUtil.getColumnIndexOrThrow(_cursor, "conditionDetails");
          final int _cursorIndexOfObservationDate = CursorUtil.getColumnIndexOrThrow(_cursor, "observationDate");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfLocalPhotoPath = CursorUtil.getColumnIndexOrThrow(_cursor, "localPhotoPath");
          final int _cursorIndexOfCloudPhotoUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "cloudPhotoUrl");
          final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isDeleted");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final PlantNote _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpPlantName;
            _tmpPlantName = _cursor.getString(_cursorIndexOfPlantName);
            final String _tmpConditionDetails;
            _tmpConditionDetails = _cursor.getString(_cursorIndexOfConditionDetails);
            final long _tmpObservationDate;
            _tmpObservationDate = _cursor.getLong(_cursorIndexOfObservationDate);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpLocalPhotoPath;
            if (_cursor.isNull(_cursorIndexOfLocalPhotoPath)) {
              _tmpLocalPhotoPath = null;
            } else {
              _tmpLocalPhotoPath = _cursor.getString(_cursorIndexOfLocalPhotoPath);
            }
            final String _tmpCloudPhotoUrl;
            if (_cursor.isNull(_cursorIndexOfCloudPhotoUrl)) {
              _tmpCloudPhotoUrl = null;
            } else {
              _tmpCloudPhotoUrl = _cursor.getString(_cursorIndexOfCloudPhotoUrl);
            }
            final boolean _tmpIsSynced;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSynced);
            _tmpIsSynced = _tmp != 0;
            final boolean _tmpIsDeleted;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsDeleted);
            _tmpIsDeleted = _tmp_1 != 0;
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final String _tmpUserId;
            _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            _result = new PlantNote(_tmpId,_tmpPlantName,_tmpConditionDetails,_tmpObservationDate,_tmpStatus,_tmpLocalPhotoPath,_tmpCloudPhotoUrl,_tmpIsSynced,_tmpIsDeleted,_tmpUpdatedAt,_tmpUserId);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getUnsyncedNotes(final Continuation<? super List<PlantNote>> $completion) {
    final String _sql = "SELECT * FROM plant_notes WHERE isSynced = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<PlantNote>>() {
      @Override
      @NonNull
      public List<PlantNote> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPlantName = CursorUtil.getColumnIndexOrThrow(_cursor, "plantName");
          final int _cursorIndexOfConditionDetails = CursorUtil.getColumnIndexOrThrow(_cursor, "conditionDetails");
          final int _cursorIndexOfObservationDate = CursorUtil.getColumnIndexOrThrow(_cursor, "observationDate");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfLocalPhotoPath = CursorUtil.getColumnIndexOrThrow(_cursor, "localPhotoPath");
          final int _cursorIndexOfCloudPhotoUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "cloudPhotoUrl");
          final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
          final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isDeleted");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final List<PlantNote> _result = new ArrayList<PlantNote>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PlantNote _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpPlantName;
            _tmpPlantName = _cursor.getString(_cursorIndexOfPlantName);
            final String _tmpConditionDetails;
            _tmpConditionDetails = _cursor.getString(_cursorIndexOfConditionDetails);
            final long _tmpObservationDate;
            _tmpObservationDate = _cursor.getLong(_cursorIndexOfObservationDate);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpLocalPhotoPath;
            if (_cursor.isNull(_cursorIndexOfLocalPhotoPath)) {
              _tmpLocalPhotoPath = null;
            } else {
              _tmpLocalPhotoPath = _cursor.getString(_cursorIndexOfLocalPhotoPath);
            }
            final String _tmpCloudPhotoUrl;
            if (_cursor.isNull(_cursorIndexOfCloudPhotoUrl)) {
              _tmpCloudPhotoUrl = null;
            } else {
              _tmpCloudPhotoUrl = _cursor.getString(_cursorIndexOfCloudPhotoUrl);
            }
            final boolean _tmpIsSynced;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSynced);
            _tmpIsSynced = _tmp != 0;
            final boolean _tmpIsDeleted;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsDeleted);
            _tmpIsDeleted = _tmp_1 != 0;
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final String _tmpUserId;
            _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            _item = new PlantNote(_tmpId,_tmpPlantName,_tmpConditionDetails,_tmpObservationDate,_tmpStatus,_tmpLocalPhotoPath,_tmpCloudPhotoUrl,_tmpIsSynced,_tmpIsDeleted,_tmpUpdatedAt,_tmpUserId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<Integer> getNotesCount() {
    final String _sql = "SELECT COUNT(*) FROM plant_notes WHERE isDeleted = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"plant_notes"}, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
