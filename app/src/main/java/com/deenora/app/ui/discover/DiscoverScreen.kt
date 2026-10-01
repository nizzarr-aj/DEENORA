package com.deenora.app.ui.discover

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deenora.app.ui.components.DeenoraTopBar
import com.deenora.app.ui.i18n.AppLanguage
import com.deenora.app.ui.i18n.AppStrings
import com.deenora.app.ui.theme.*

private enum class ExtraPage { NONE, DUAS, STORIES, QUIZ }

private data class DeenoraDua(
    val ar: String,
    val en: String,
    val fr: String,
    val source: String
)

private data class DeenoraStory(
    val arTitle: String,
    val enTitle: String,
    val frTitle: String,
    val ar: String,
    val en: String,
    val fr: String
)

private data class DeenoraQuestion(
    val ar: String,
    val en: String,
    val fr: String,
    val answersAr: List<String>,
    val answersEn: List<String>,
    val answersFr: List<String>,
    val correct: Int
)

private val deenoraDuas = listOf(
    DeenoraDua(
        "رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً وَفِي الْآخِرَةِ حَسَنَةً وَقِنَا عَذَابَ النَّارِ",
        "Our Lord, give us good in this world and good in the Hereafter, and protect us from the punishment of the Fire.",
        "Seigneur, accorde-nous un bien ici-bas et un bien dans l’au-delà, et protège-nous du châtiment du Feu.",
        "Qur'an 2:201"
    ),
    DeenoraDua(
        "رَبِّ زِدْنِي عِلْمًا",
        "My Lord, increase me in knowledge.",
        "Seigneur, augmente-moi en connaissance.",
        "Qur'an 20:114"
    ),
    DeenoraDua(
        "رَبَّنَا لَا تُزِغْ قُلُوبَنَا بَعْدَ إِذْ هَدَيْتَنَا وَهَبْ لَنَا مِنْ لَدُنْكَ رَحْمَةً",
        "Our Lord, do not let our hearts deviate after You have guided us, and grant us mercy from You.",
        "Seigneur, ne détourne pas nos cœurs après nous avoir guidés et accorde-nous une miséricorde venant de Toi.",
        "Qur'an 3:8"
    ),
    DeenoraDua(
        "رَبِّ اشْرَحْ لِي صَدْرِي ۝ وَيَسِّرْ لِي أَمْرِي",
        "My Lord, expand my chest for me and make my task easy for me.",
        "Seigneur, ouvre-moi la poitrine et facilite-moi ma tâche.",
        "Qur'an 20:25–26"
    ),
    DeenoraDua(
        "اللَّهُمَّ إِنَّكَ عَفُوٌّ تُحِبُّ الْعَفْوَ فَاعْفُ عَنِّي",
        "O Allah, You are Pardoning and love to pardon, so pardon me.",
        "Ô Allah, Tu es Pardonneur et Tu aimes pardonner, alors pardonne-moi.",
        "Jami` at-Tirmidhi 3513"
    ),
    DeenoraDua(
        "حَسْبِيَ اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ ۖ عَلَيْهِ تَوَكَّلْتُ وَهُوَ رَبُّ الْعَرْشِ الْعَظِيمِ",
        "Allah is sufficient for me. There is no deity except Him. Upon Him I rely.",
        "Allah me suffit. Il n’y a de divinité que Lui. En Lui je place ma confiance.",
        "Qur'an 9:129"
    )
)

private val deenoraStories = listOf(
    DeenoraStory(
        "قصة يوسف عليه السلام",
        "The Story of Yusuf",
        "L’histoire de Yusuf",
        "ابتُلي يوسف عليه السلام بالحسد والفراق والسجن، ثم جعل الله بعد الصبر فرجًا ومكّنه في الأرض. تذكّر القصة بقوة الصبر والعفو والثقة بالله.",
        "Yusuf faced jealousy, separation and imprisonment, then Allah brought relief after his patience and gave him a position of strength. The story highlights patience, forgiveness and trust in Allah.",
        "Yusuf a connu la jalousie, la séparation et la prison, puis Allah lui a accordé le soulagement après sa patience. L’histoire rappelle la patience, le pardon et la confiance en Allah."
    ),
    DeenoraStory(
        "موسى عليه السلام والبحر",
        "Musa and the Sea",
        "Musa et la mer",
        "لما اشتد الخوف على موسى عليه السلام ومن معه، جاء وعد الله بالنجاة. فانشق البحر بأمر الله، ونجا المؤمنون.",
        "When fear became intense for Musa and those with him, Allah's promise of rescue came. The sea was parted by Allah's command and the believers were saved.",
        "Lorsque la peur devint intense pour Musa et ceux qui étaient avec lui, la promesse d’Allah arriva. La mer fut fendue par Son ordre et les croyants furent sauvés."
    ),
    DeenoraStory(
        "أصحاب الكهف",
        "The People of the Cave",
        "Les gens de la caverne",
        "آمن فتية بالله وثبتوا على إيمانهم، فآواهم الله إلى الكهف وحفظهم. وتعلّم القصة الثبات على الحق وحسن التوكل.",
        "A group of young believers remained firm in faith, and Allah sheltered and protected them in the cave. The story teaches steadfastness and reliance upon Allah.",
        "Un groupe de jeunes croyants resta ferme dans sa foi, et Allah les abrita et les protégea dans la caverne. L’histoire enseigne la fermeté et la confiance en Allah."
    ),
    DeenoraStory(
        "أيوب عليه السلام",
        "The Story of Ayyub",
        "L’histoire de Ayyub",
        "اشتد البلاء على أيوب عليه السلام، فصبر ودعا ربه دون يأس. ثم كشف الله عنه الضر وأكرمه برحمته.",
        "Ayyub endured severe trials, remained patient and called upon his Lord without despair. Allah then removed his hardship and honored him with mercy.",
        "Ayyub a subi de grandes épreuves, mais il est resté patient et a invoqué son Seigneur sans désespoir. Allah a ensuite éloigné son malheur et lui a accordé Sa miséricorde."
    )
)

private val deenoraQuestions = listOf(
    DeenoraQuestion(
        "كم عدد الصلوات المفروضة في اليوم والليلة؟",
        "How many obligatory prayers are there each day and night?",
        "Combien de prières obligatoires y a-t-il chaque jour et chaque nuit ?",
        listOf("3", "5", "7", "10"),
        listOf("3", "5", "7", "10"),
        listOf("3", "5", "7", "10"),
        1
    ),
    DeenoraQuestion(
        "ما هي أطول سورة في القرآن؟",
        "Which is the longest surah in the Qur'an?",
        "Quelle est la plus longue sourate du Coran ?",
        listOf("الفاتحة", "البقرة", "يس", "الملك"),
        listOf("Al-Fatihah", "Al-Baqarah", "Ya-Sin", "Al-Mulk"),
        listOf("Al-Fatiha", "Al-Baqara", "Ya-Sin", "Al-Mulk"),
        1
    ),
    DeenoraQuestion(
        "كم عدد أسماء الله الحسنى المشهورة؟",
        "How many well-known Names of Allah are commonly listed?",
        "Combien de Noms d’Allah sont couramment répertoriés ?",
        listOf("25", "50", "99", "114"),
        listOf("25", "50", "99", "114"),
        listOf("25", "50", "99", "114"),
        2
    ),
    DeenoraQuestion(
        "في أي شهر يصوم المسلمون صيام رمضان؟",
        "In which month do Muslims fast Ramadan?",
        "Pendant quel mois les musulmans jeûnent-ils le Ramadan ?",
        listOf("شعبان", "رمضان", "شوال", "محرم"),
        listOf("Sha'ban", "Ramadan", "Shawwal", "Muharram"),
        listOf("Chaabane", "Ramadan", "Chawwal", "Mouharram"),
        1
    ),
    DeenoraQuestion(
        "ما هي السورة التي تبدأ بـ الحمد لله رب العالمين؟",
        "Which surah begins with 'Praise be to Allah, Lord of the worlds'?",
        "Quelle sourate commence par « Louange à Allah, Seigneur des mondes » ?",
        listOf("الفاتحة", "الإخلاص", "الناس", "الكوثر"),
        listOf("Al-Fatihah", "Al-Ikhlas", "An-Nas", "Al-Kawthar"),
        listOf("Al-Fatiha", "Al-Ikhlas", "An-Nas", "Al-Kawthar"),
        0
    )
)

@Composable
fun DiscoverScreen(
    viewModel: DiscoverViewModel,
    currentLanguage: AppLanguage,
    onLanguageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    var extraPage by remember { mutableStateOf(ExtraPage.NONE) }

    when {
        extraPage == ExtraPage.DUAS -> {
            ExtrasDuasScreen(currentLanguage) { extraPage = ExtraPage.NONE }
        }
        extraPage == ExtraPage.STORIES -> {
            ExtrasStoriesScreen(currentLanguage) { extraPage = ExtraPage.NONE }
        }
        extraPage == ExtraPage.QUIZ -> {
            ExtrasQuizScreen(currentLanguage) { extraPage = ExtraPage.NONE }
        }
        state.currentSection == DiscoverSection.QIBLA -> {
            QiblaCompassView(
                viewModel = viewModel,
                currentLanguage = currentLanguage,
                onBackClick = { viewModel.navigateToSection(DiscoverSection.HUB) }
            )
        }
        state.currentSection == DiscoverSection.TASBIH -> {
            TasbihView(
                viewModel = viewModel,
                currentLanguage = currentLanguage,
                onBackClick = { viewModel.navigateToSection(DiscoverSection.HUB) }
            )
        }
        state.currentSection == DiscoverSection.NAMES_OF_ALLAH -> {
            NamesOfAllahView(
                viewModel = viewModel,
                currentLanguage = currentLanguage,
                onBackClick = { viewModel.navigateToSection(DiscoverSection.HUB) }
            )
        }
        state.currentSection == DiscoverSection.CALENDAR -> {
            IslamicCalendarView(
                currentLanguage = currentLanguage,
                hijriAdjustment = state.settings.hijriAdjustment,
                onBackClick = { viewModel.navigateToSection(DiscoverSection.HUB) }
            )
        }
        else -> {
            Scaffold(
                modifier = modifier.fillMaxSize(),
                topBar = {
                    DeenoraTopBar(
                        title = AppStrings.navDiscover(currentLanguage),
                        subtitle = when (currentLanguage) {
                            AppLanguage.ARABIC -> "القبلة • التسبيح • الأسماء الحسنى • المزيد"
                            AppLanguage.ENGLISH -> "Qibla • Tasbih • 99 Names • More"
                            AppLanguage.FRENCH -> "Qibla • Tasbih • 99 Noms • Plus"
                        },
                        currentLanguage = currentLanguage,
                        onLanguageClick = onLanguageClick
                    )
                }
            ) { innerPadding ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(top = 16.dp, bottom = 28.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    item {
                        DiscoverCardItem(
                            title = AppStrings.qiblaCompass(currentLanguage),
                            description = when (currentLanguage) {
                                AppLanguage.ARABIC -> "بوصلة دقيقة لتحديد اتجاه الكعبة المشرفة مع المسافة"
                                AppLanguage.ENGLISH -> "Compass pointing to the Kaaba with live distance"
                                AppLanguage.FRENCH -> "Boussole indiquant la direction de la Kaaba et la distance"
                            },
                            icon = Icons.Outlined.Explore,
                            accentColor = EmeraldPrimary,
                            tag = "discover_qibla_card",
                            onClick = { viewModel.navigateToSection(DiscoverSection.QIBLA) }
                        )
                    }

                    item {
                        DiscoverCardItem(
                            title = AppStrings.digitalTasbih(currentLanguage),
                            description = when (currentLanguage) {
                                AppLanguage.ARABIC -> "سبحة ذكية مع اهتزاز وسجل يومي وهدف للتسبيح"
                                AppLanguage.ENGLISH -> "Smart digital tasbih with haptics, history and daily goals"
                                AppLanguage.FRENCH -> "Chapelet numérique avec vibrations, historique et objectifs"
                            },
                            icon = Icons.Outlined.FilterVintage,
                            accentColor = GoldPrimary,
                            tag = "discover_tasbih_card",
                            onClick = { viewModel.navigateToSection(DiscoverSection.TASBIH) }
                        )
                    }

                    item {
                        DiscoverCardItem(
                            title = AppStrings.namesOfAllah(currentLanguage),
                            description = when (currentLanguage) {
                                AppLanguage.ARABIC -> "الأسماء الحسنى التسعة والتسعون مع المعاني"
                                AppLanguage.ENGLISH -> "The 99 Beautiful Names of Allah with meanings"
                                AppLanguage.FRENCH -> "Les 99 Plus Beaux Noms d'Allah avec significations"
                            },
                            icon = Icons.Outlined.AutoAwesome,
                            accentColor = Color(0xFF236858),
                            tag = "discover_names_card",
                            onClick = { viewModel.navigateToSection(DiscoverSection.NAMES_OF_ALLAH) }
                        )
                    }

                    item {
                        DiscoverCardItem(
                            title = AppStrings.islamicCalendar(currentLanguage),
                            description = when (currentLanguage) {
                                AppLanguage.ARABIC -> "التقويم الهجري والمناسبات والأيام الإسلامية"
                                AppLanguage.ENGLISH -> "Hijri calendar and important Islamic dates"
                                AppLanguage.FRENCH -> "Calendrier hégirien et dates islamiques importantes"
                            },
                            icon = Icons.Outlined.CalendarMonth,
                            accentColor = Color(0xFFB57022),
                            tag = "discover_calendar_card",
                            onClick = { viewModel.navigateToSection(DiscoverSection.CALENDAR) }
                        )
                    }

                    item {
                        DiscoverCardItem(
                            title = when (currentLanguage) {
                                AppLanguage.ARABIC -> "الأدعية"
                                AppLanguage.ENGLISH -> "Duas"
                                AppLanguage.FRENCH -> "Invocations"
                            },
                            description = when (currentLanguage) {
                                AppLanguage.ARABIC -> "أدعية مختارة مع مصادرها وترجمتها"
                                AppLanguage.ENGLISH -> "Selected duas with sources and translations"
                                AppLanguage.FRENCH -> "Invocations sélectionnées avec sources et traductions"
                            },
                            icon = Icons.Outlined.FavoriteBorder,
                            accentColor = Color(0xFF9A5B73),
                            tag = "discover_duas_card",
                            onClick = { extraPage = ExtraPage.DUAS }
                        )
                    }

                    item {
                        DiscoverCardItem(
                            title = when (currentLanguage) {
                                AppLanguage.ARABIC -> "قصص إسلامية"
                                AppLanguage.ENGLISH -> "Islamic Stories"
                                AppLanguage.FRENCH -> "Histoires islamiques"
                            },
                            description = when (currentLanguage) {
                                AppLanguage.ARABIC -> "قصص مختارة عن الأنبياء والصبر والثقة بالله"
                                AppLanguage.ENGLISH -> "Short stories about prophets, patience and trust in Allah"
                                AppLanguage.FRENCH -> "Histoires sur les prophètes, la patience et la confiance en Allah"
                            },
                            icon = Icons.Outlined.AutoStories,
                            accentColor = Color(0xFF6C63A8),
                            tag = "discover_stories_card",
                            onClick = { extraPage = ExtraPage.STORIES }
                        )
                    }

                    item {
                        DiscoverCardItem(
                            title = when (currentLanguage) {
                                AppLanguage.ARABIC -> "اختبر معلوماتك"
                                AppLanguage.ENGLISH -> "Islamic Quiz"
                                AppLanguage.FRENCH -> "Quiz islamique"
                            },
                            description = when (currentLanguage) {
                                AppLanguage.ARABIC -> "اختبار تفاعلي واحسب نتيجتك في النهاية"
                                AppLanguage.ENGLISH -> "Interactive questions with a final score"
                                AppLanguage.FRENCH -> "Questions interactives avec score final"
                            },
                            icon = Icons.Outlined.Quiz,
                            accentColor = Color(0xFF2E7D8A),
                            tag = "discover_quiz_card",
                            onClick = { extraPage = ExtraPage.QUIZ }
                        )
                    }

                    item {
                        PremiumInfoCard(currentLanguage)
                    }
                }
            }
        }
    }
}

@Composable
fun DiscoverCardItem(
    title: String,
    description: String,
    icon: ImageVector,
    accentColor: Color,
    tag: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag(tag)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, accentColor.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = accentColor,
                    modifier = Modifier.size(28.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp
                )
            }

            Icon(
                imageVector = Icons.Outlined.ChevronRight,
                contentDescription = null,
                tint = accentColor
            )
        }
    }
}

@Composable
private fun PremiumInfoCard(language: AppLanguage) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = EmeraldPrimary.copy(alpha = 0.10f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.35f))
    ) {
        Column(Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.Star,
                    contentDescription = null,
                    tint = GoldPrimary
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    when (language) {
                        AppLanguage.ARABIC -> "DEENORA Premium"
                        AppLanguage.ENGLISH -> "DEENORA Premium"
                        AppLanguage.FRENCH -> "DEENORA Premium"
                    },
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                when (language) {
                    AppLanguage.ARABIC -> "مكان جاهز لإضافة المزايا المدفوعة لاحقًا: بدون إعلانات، مزايا إضافية ومحتوى موسّع."
                    AppLanguage.ENGLISH -> "Premium area ready for future Google Play Billing features such as ad-free use and expanded content."
                    AppLanguage.FRENCH -> "Espace Premium prêt pour de futures fonctions Google Play Billing et du contenu étendu."
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExtrasDuasScreen(language: AppLanguage, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (language == AppLanguage.ARABIC) "الأدعية" else if (language == AppLanguage.ENGLISH) "Duas" else "Invocations") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Outlined.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(deenoraDuas) { dua ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(Modifier.padding(18.dp)) {
                        Text(
                            dua.ar,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(10.dp))
                        Text(
                            when (language) {
                                AppLanguage.ARABIC -> ""
                                AppLanguage.ENGLISH -> dua.en
                                AppLanguage.FRENCH -> dua.fr
                            },
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            dua.source,
                            style = MaterialTheme.typography.labelSmall,
                            color = GoldDark,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExtrasStoriesScreen(language: AppLanguage, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (language == AppLanguage.ARABIC) "قصص إسلامية" else if (language == AppLanguage.ENGLISH) "Islamic Stories" else "Histoires islamiques") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Outlined.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(deenoraStories) { story ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(Modifier.padding(18.dp)) {
                        Text(story.arTitle, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text(
                            when (language) {
                                AppLanguage.ARABIC -> story.ar
                                AppLanguage.ENGLISH -> "${story.enTitle}\n\n${story.en}"
                                AppLanguage.FRENCH -> "${story.frTitle}\n\n${story.fr}"
                            },
                            style = MaterialTheme.typography.bodyLarge,
                            lineHeight = 25.sp
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExtrasQuizScreen(language: AppLanguage, onBack: () -> Unit) {
    var questionIndex by remember { mutableIntStateOf(0) }
    var selected by remember { mutableIntStateOf(-1) }
    var score by remember { mutableIntStateOf(0) }
    var finished by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (language == AppLanguage.ARABIC) "اختبار إسلامي" else if (language == AppLanguage.ENGLISH) "Islamic Quiz" else "Quiz islamique") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Outlined.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (finished) {
            Column(
                Modifier.fillMaxSize().padding(padding).padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(Icons.Outlined.EmojiEvents, contentDescription = null, modifier = Modifier.size(64.dp), tint = GoldPrimary)
                Spacer(Modifier.height(16.dp))
                Text(
                    if (language == AppLanguage.ARABIC) "أحسنت!" else if (language == AppLanguage.ENGLISH) "Well done!" else "Bravo !",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(10.dp))
                Text("$score / ${deenoraQuestions.size}", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(22.dp))
                Button(onClick = {
                    questionIndex = 0
                    selected = -1
                    score = 0
                    finished = false
                }) {
                    Text(if (language == AppLanguage.ARABIC) "إعادة الاختبار" else if (language == AppLanguage.ENGLISH) "Restart" else "Recommencer")
                }
            }
        } else {
            val q = deenoraQuestions[questionIndex]
            val questionText = when (language) {
                AppLanguage.ARABIC -> q.ar
                AppLanguage.ENGLISH -> q.en
                AppLanguage.FRENCH -> q.fr
            }
            val answers = when (language) {
                AppLanguage.ARABIC -> q.answersAr
                AppLanguage.ENGLISH -> q.answersEn
                AppLanguage.FRENCH -> q.answersFr
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    LinearProgressIndicator(
                        progress = { (questionIndex + 1).toFloat() / deenoraQuestions.size.toFloat() },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "${questionIndex + 1} / ${deenoraQuestions.size}",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Text(
                            questionText,
                            Modifier.padding(20.dp),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                items(answers.indices.toList()) { index ->
                    FilterChip(
                        selected = selected == index,
                        onClick = { selected = index },
                        label = { Text(answers[index]) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    Button(
                        onClick = {
                            if (selected == q.correct) score++
                            if (questionIndex == deenoraQuestions.lastIndex) {
                                finished = true
                            } else {
                                questionIndex++
                                selected = -1
                            }
                        },
                        enabled = selected >= 0,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            if (questionIndex == deenoraQuestions.lastIndex) {
                                if (language == AppLanguage.ARABIC) "إنهاء الاختبار" else if (language == AppLanguage.ENGLISH) "Finish" else "Terminer"
                            } else {
                                if (language == AppLanguage.ARABIC) "السؤال التالي" else if (language == AppLanguage.ENGLISH) "Next" else "Suivant"
                            }
                        )
                    }
                }
            }
        }
    }
}
