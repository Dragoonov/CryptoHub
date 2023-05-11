package com.moonlightbutterfly.cryptohub.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.moonlightbutterfly.cryptohub.data.common.Answer
import com.moonlightbutterfly.cryptohub.data.localpreferences.LocalPreferencesRepository
import com.moonlightbutterfly.cryptohub.models.LocalPreferences
import com.moonlightbutterfly.cryptohub.models.NotificationConfiguration
import com.moonlightbutterfly.cryptohub.models.NotificationInterval
import com.moonlightbutterfly.cryptohub.models.NotificationTime
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Test

class NotifierImplTest {
    private val workManager: WorkManager = mockk {
        every { cancelAllWorkByTag(any()) } returns mockk()
        every { enqueue(any<WorkRequest>()) } returns mockk()
    }
    private val alarmManager: AlarmManager = mockk {
        every { cancel(any<PendingIntent>()) } just Runs
        every { setRepeating(any(), any(), any(), any()) } just Runs
    }
    private val context: Context = mockk {
        every { getSystemService(Context.ALARM_SERVICE) } returns alarmManager
    }
    private val localPreferencesRepository: LocalPreferencesRepository = mockk {
        every { getLocalPreferences() } returns flowOf(
            Answer.Success(
                LocalPreferences(
                    notificationsConfiguration = setOf(
                        NotificationConfiguration("ETH", NotificationInterval.WEEK, null),
                        NotificationConfiguration("ADA", null, NotificationTime(20, 20)),
                    )
                )
            )
        )
    }

    private val notifier = NotifierImpl(context, localPreferencesRepository, workManager)

    @Before
    fun mockStatic() {
        mockkStatic(PendingIntent::class)
        every { PendingIntent.getBroadcast(any(), any(), any(), any()) } returns mockk()
    }

    @Test
    fun `should configure notifications`() {
        // GIVEN
        val notifications = setOf(
            NotificationConfiguration("BTC", NotificationInterval.DAY, NotificationTime(20, 20)),
            NotificationConfiguration("ETH", NotificationInterval.WEEK, null),
            NotificationConfiguration("ADA", null, NotificationTime(20, 20)),
        )

        // WHEN
        notifier.configure(notifications)

        // THEN
        verify {
            workManager.cancelAllWorkByTag(any())
            localPreferencesRepository.getLocalPreferences()
            context.getSystemService(Context.ALARM_SERVICE)
            alarmManager.cancel(any<PendingIntent>())
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, any(), AlarmManager.INTERVAL_DAY, any())
            workManager.enqueue(any<WorkRequest>())
        }
    }
}
