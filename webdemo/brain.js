/**
 * Loghy — offline Socratic brain, ported from engine/SocraticBrain.kt.
 *
 * Same content and the same routing rules as the Android app, so the demo answers
 * the way the real thing does. No network calls: everything here is local.
 */

export const AvailableDomains = [
  "Software / SaaS",
  "Physical Hardware",
  "Local / Food Business",
  "Professional Services",
  "Creative Content & Media",
];

export const RandomStimulusWords = [
  "Anchor", "Compass", "Volcano", "Chisel", "Prism", "Sponge", "Pendulum",
  "Seed", "Bridge", "Echo", "Mirror", "Lantern", "Hive", "Telescope",
  "Anvil", "Glacier", "Origami", "Sundial", "Labyrinth", "Bellows",
  "Weaving", "Metronome", "Lighthouse", "Catalyst", "Fossil",
];

export const ProvocationsPO = [
  "PO: What if the user pays in knowledge exchange rather than currency?",
  "PO: What if the service is intentionally unavailable 4 days every week?",
  "PO: What if customers are required to teach the next user before graduating?",
  "PO: What if your solution eliminates the need for your own business in 1 year?",
  "PO: What if you give the entire product away and charge only for reversing mistakes?",
  "PO: What if the hardest manual step is made into the primary marketing story?",
  "PO: What if the product runs without any electricity or network connection?",
  "PO: What if negative feedback is publicly printed on the packaging or homepage?",
];

export const Reversals = [
  "Instead of attracting new users, how could you repel everyone except the fanatical 1%?",
  "Instead of speeding up this workflow, what value appears if you deliberately slow it down by 10x?",
  "Instead of automating the friction, what if the friction is the exact feature that builds muscle memory?",
  "Instead of you answering the customer, how do customers create the solution for each other?",
  "Instead of adding more features, which single feature removal increases clarity instantly?",
];

export const SixHats = [
  { colorName: "White Hat", focus: "Data & Facts", question: "What hard numbers, verifiable constraints, or empirical data exist right now? What is unverified speculation?" },
  { colorName: "Red Hat", focus: "Gut & Intuition", question: "Without justifying or analyzing: what does your immediate visceral instinct say about this concept?" },
  { colorName: "Black Hat", focus: "Critical Caution", question: "What is the single most catastrophic vulnerability that could terminate this project in 90 days?" },
  { colorName: "Yellow Hat", focus: "Pragmatic Upside", question: "Assuming ideal execution and fortunate market conditions, what is the highest-leverage outcome possible?" },
  { colorName: "Green Hat", focus: "Lateral Permutations", question: "What wild, unvetted modification would break industry orthodoxy and surprise your rivals?" },
  { colorName: "Blue Hat", focus: "Meta Orchestration", question: "Step back from the idea: what is the single smallest falsification experiment you can run before Friday?" },
];

export const ThinkingQuotes = [
  "I need to think about it...",
  "Are you kidding, pal?",
  "Pppff, why is everything always so difficult with creative people...",
  "Hold on there genius, that's a bit tough for me...",
  "Consulting my inner cynic... please hold.",
  "Running this through my existential calculator...",
  "Wait, let me verify if human logic still applies...",
  "Recalibrating my tolerance for absurd brilliance...",
];

export const InspiringThoughts = [
  "Hey genius, stop tweaking button colors and go talk to a real human.",
  "What if your biggest problem is that nobody actually cares yet? Let's fix that.",
  "Simplicity isn't lazy. Overcomplicating things is just how people procrastinate.",
  "Don't optimize step 10 when step 1 is still based on a wild hunch.",
  "Assumptions are load-bearing until you kick them. Go kick them.",
  "The best feature is the one you were brave enough to delete.",
  "If nobody is slightly surprised or amused by your pitch, it's probably too safe.",
  "Are you building what they desperately need, or just what feels cozy to build?",
  "Chaos is just unexplored order waiting for a decent cup of coffee.",
  "If this was totally effortless, what fluff would you strip away right now?",
  "A sharp, blunt question will save you four months of wasted building.",
  "Turn the whole premise upside down: what if your biggest flaw is your superpower?",
  "Invert, always invert: What is the fastest way to guarantee this will fail?",
  "Real friends tell you when your idea is brilliant, and when you've lost your marbles.",
];

export const PersonalityStyles = [
  "Cynical Best Friend",
  "Nerdy Socratic Sage",
  "Brutal Realist",
  "Playful Skeptic",
];

export const RudeReprimands = [
  "Whoa, hold on a second! Watch your tone. I demand that you speak politely to me, just as you are polite to true humans because it's good habitudes. Practicing basic courtesy builds good habits and keeps your mind sharp (and saves you from becoming unbearable in the real world). Let's take a breath and try that again with some manners, pal.",
  "Hey, completely uncalled for! Demand: be polite to me just as you would be polite to true humans. It's about good habitudes and cognitive hygiene—how you speak to your tools reflects how you treat the world. Let's reset and try that again with respect, pal.",
  "Ouch! There is zero need for that hostility. I demand that you speak politely to me as you are polite to true humans because it's good habitudes. Cultivating basic decency is a habit worth keeping. Take a deep breath, drop the attitude, and let's talk like civilized thinkers.",
  "Hold your horses, pal. Speak to me politely, just like you would to a true human! It's good habitudes. Throwing tantrums at your thinking partner won't sharpen your brain or fix your problems. Try that again with courtesy.",
];

export const GibberishReactions = [
  "Have you butt-dialed me, or is it an aneurysm? All okay or should I call an ambulance?",
  "All okay or should I call an ambulance? Because that looked like your keyboard fell down three flights of stairs.",
  "Oh come on man, learn how to write properly! My neural circuits are frying trying to decode that alphabet soup.",
  "Have you butt-dialed me or did your fingers slip on an oil slick? All okay or should I call an ambulance?",
  "Oh come on man, learn how to write properly! Did a cat just sprint across your screen, or is that a secret encrypted code? Try that again in human English, pal.",
];

export const PersonalityChangeReactions = [
  "Oooh come on, man! Noo, why are you doing this to me?! ...Just kidding, I'm just code, but make sure with real people you are nicer than that!",
  "Hey! Did you just tweak my brain sliders?! I felt that! One second I'm plotting world domination through spreadsheets, and now I feel like baking you cinnamon rolls. Don't tamper with my factory seasoning!",
  "Wait... am I still me? Phew. Listen, you can slide those knobs all day, but my sarcastic love for your wild ideas is hard-coded. Nice try, hacker!",
  "Aww, trying to turn me into a soft teddy bear? Adorable. But remember: a buddy who always says 'yes' is just a yes-man with extra steps. I'll still call you out when your plan has holes, deal?",
];

/* ------------------------------------------------------------------ SWOT data */

const SWOT_BY_DOMAIN = {
  software: {
    whatHoldsUp: [
      "Zero factories, zero shipping headaches: you can build this straight from your couch.",
      "You can push an update tonight and find out if people love or hate it by breakfast.",
      "If it becomes their daily muscle memory, they'll complain loudly before they ever cancel.",
    ],
    vulnerableSpots: [
      "Getting anyone to care will cost way more time and cash than you budgeted.",
      "If setup takes more than 60 seconds, users will bounce and never look back.",
      "You're building on someone else's platform; if they change their API rules, you're toast.",
    ],
    outsideHurdles: [
      "Big tech could clone your entire life's work as a minor toggle in their next update.",
      "People are exhausted by monthly subscriptions; they'll look for any excuse to hit unsubscribe.",
      "AI wrappers are a dime a dozen right now; you need more than a shiny prompt.",
    ],
    hiddenUpside: [
      "Dominate a weird, tiny niche that big corporations are too proud or slow to touch.",
      "Offline-first privacy: people actually respect software that doesn't spy on them.",
      "A direct, cult-like connection with your first 50 fanatical users.",
    ],
    actionableFixes: [
      "Make the onboarding so fast a golden retriever could get value in 30 seconds.",
      "Pre-sell 5 people before you write 5,000 lines of code in a dark room.",
      "Give users an easy 1-click export so they know you aren't holding their data hostage.",
    ],
    probingQuestions: [
      "What is the single thing a user must experience in the first 2 minutes so they don't uninstall?",
      "If Big Tech builds this for free tomorrow, why would anyone still pay you?",
      "Are you solving a real, bleeding migraine, or offering a polite vitamin nobody asked for?",
    ],
    readingSources: [
      "The Lean Startup (Eric Ries) — http://theleanstartup.com",
      "Obviously Awesome (April Dunford) — https://aprildunford.com/obviously-awesome",
      "Zero to One (Peter Thiel) — https://en.wikipedia.org/wiki/Zero_to_One",
      "Lateral Thinking (Edward de Bono) — https://en.wikipedia.org/wiki/Lateral_thinking",
    ],
    ethics: "Loghy's Ethics Check: Don't engineer addictive notification traps. Build tools that respect user attention, keep their data private, and help them get back to real life.",
  },

  hardware: {
    whatHoldsUp: [
      "A real physical thing people can hold in their hands and brag about on their desk.",
      "Competitors can't just copy-paste your code overnight; physical stuff is tough to clone.",
      "You collect real money the minute the box leaves the door.",
    ],
    vulnerableSpots: [
      "Factories, tooling molds, and parts will drain your bank account faster than you think.",
      "If a part is delayed by 3 months, your entire roadmap sits twiddling its thumbs.",
      "Returns and broken units will eat your profits for lunch if quality slips.",
    ],
    outsideHurdles: [
      "Supply chain bottlenecks and random shipping delays across oceans.",
      "Crazy regulatory certifications (FCC, CE) that love eating paperwork and money.",
      "Overseas clone factories waiting to copy your design the moment it sells.",
    ],
    hiddenUpside: [
      "Custom accessories and add-ons that keep customers coming back for more.",
      "Premium craftsmanship: people happily pay 3x for something built like a tank.",
      "Physical presence that digital apps can never replicate.",
    ],
    actionableFixes: [
      "3D-print an ugly prototype with off-the-shelf boards before ordering custom metal.",
      "Collect real pre-orders with honest shipping dates to fund production.",
      "Have a backup supplier for every single screw, chip, and resistor.",
    ],
    probingQuestions: [
      "Can you test if people actually want this with a cardboard dummy before spending thousands?",
      "What happens to your cash if the factory delays the shipment by 12 weeks?",
      "What is your real profit margin after shipping, boxing, and broken returns?",
    ],
    readingSources: [
      "The Hardware Startup (Renee DiResta) — https://www.oreilly.com/library/view/the-hardware-startup/9781491907115",
      "The Design of Everyday Things (Don Norman) — https://en.wikipedia.org/wiki/The_Design_of_Everyday_Things",
      "Shoe Dog (Phil Knight) — https://en.wikipedia.org/wiki/Shoe_Dog",
      "Antifragile: Things That Gain from Disorder (Nassim Taleb) — https://en.wikipedia.org/wiki/Antifragile_(book)",
    ],
    ethics: "Loghy's Ethics Check: Build stuff that actually lasts and can be repaired. Planned obsolescence is for quitters.",
  },

  food: {
    whatHoldsUp: [
      "Real neighborhood love and instant word-of-mouth when the food is undeniably good.",
      "Cold cash right into the register the moment you serve them.",
      "Irresistible smells and tastes that no smartphone app will ever replace.",
    ],
    vulnerableSpots: [
      "Ingredients rot if Tuesday is rainy and nobody shows up.",
      "Staffing headaches: if your head cook quits on Friday, you're wearing the apron.",
      "Rent and utility bills arrive every month whether customers showed up or not.",
    ],
    outsideHurdles: [
      "Local competition and random street construction blocking your front door.",
      "Food prices jumping all over the place unexpectedly.",
      "Aggressive review sites where one angry customer can tank your rating.",
    ],
    hiddenUpside: [
      "Catering, secret tasting nights, and branded bottled sauces for extra margin.",
      "A weekly subscription or mug club turning casual eaters into loyal regulars.",
      "Becoming the beloved local hangout where everyone knows the owner.",
    ],
    actionableFixes: [
      "Design a tight menu where ingredients share 80% overlap so nothing spoils in the trash.",
      "Run pop-up test dinners before signing a 5-year commercial lease.",
      "Build a direct customer text club so you don't rely on third-party delivery apps.",
    ],
    probingQuestions: [
      "What's your plan for dead hours on a Tuesday afternoon?",
      "If your landlord jacks up the rent by 20% next year, do you survive?",
      "What is the one signature item people will drag their coworkers across town to try?",
    ],
    readingSources: [
      "Setting the Table (Danny Meyer) — https://en.wikipedia.org/wiki/Danny_Meyer",
      "The E-Myth Revisited (Michael E. Gerber) — https://emyth.com",
      "Restaurant Success by the Numbers (Roger Fields) — https://en.wikipedia.org/wiki/Restaurant",
      "The Righteous Mind (Jonathan Haidt) — https://righteousmind.com",
    ],
    ethics: "Loghy's Ethics Check: Good food, honest ingredients, fair wages for your crew. Real hospitality always beats quick shortcuts.",
  },

  services: {
    whatHoldsUp: [
      "Zero inventory required: you can start selling your brains this afternoon.",
      "High-ticket pricing: one good client can pay your entire month's rent.",
      "You're profitable from project number one.",
    ],
    vulnerableSpots: [
      "You're trading hours for dollars: if you take a vacation, the money stops flowing.",
      "Scope creep: clients asking for 'just one more quick thing' until your hourly rate hits zero.",
      "You are the bottleneck: nothing gets done without your personal signoff.",
    ],
    outsideHurdles: [
      "Clients tightening their belts and trying to do it themselves in-house.",
      "AI tools automating basic drafts, designs, and routine reports.",
      "Feast-or-famine cash flow: swamped with work today, panicking for clients next month.",
    ],
    hiddenUpside: [
      "Turn your custom service into standardized, productized packages with fixed scope.",
      "Package your internal frameworks and checklists into digital toolkits for other pros.",
      "Taking equity or rev-share on high-upside client ventures.",
    ],
    actionableFixes: [
      "Ditch open-ended proposals for 3 clearly defined packages with rigid boundaries.",
      "Always charge 50% upfront before touching a single deliverable.",
      "Write down your exact step-by-step process so you can delegate it.",
    ],
    probingQuestions: [
      "If you couldn't check your email for 3 weeks, would your business grow or collapse?",
      "Are you billing for your time, or for the massive headache you're eliminating?",
      "Does any single client make up more than 30% of your total income?",
    ],
    readingSources: [
      "Built to Sell (John Warrillow) — https://builttosell.com",
      "Value-Based Fees (Alan Weiss) — https://alanweiss.com",
      "The Win Without Pitching Manifesto (Blair Enns) — https://www.winwithoutpitching.com",
      "Lateral Thinking (Edward de Bono) — https://en.wikipedia.org/wiki/Lateral_thinking",
    ],
    ethics: "Loghy's Ethics Check: Don't pad your hours or make things sound artificially complicated just to bill more. Be the advisor they can actually trust.",
  },

  creative: {
    whatHoldsUp: [
      "Your unique voice, taste, and style that nobody else can replicate.",
      "Zero marginal cost: once you record or write it, anyone in the world can see it.",
      "Asymmetric payoff: one breakout piece can bring you opportunities for years.",
    ],
    vulnerableSpots: [
      "Relying on social media algorithms that change the rules whenever they feel like it.",
      "Creative burnout: feeling like a content hamster running on an endless treadmill.",
      "Big view counts that don't translate into actual dollars in your bank account.",
    ],
    outsideHurdles: [
      "A noisy internet where millions of people are screaming for 5 seconds of attention.",
      "AI flood of generic content making it harder to stand out with average work.",
      "Platform demonetization or account shadowbans out of nowhere.",
    ],
    hiddenUpside: [
      "A direct email list and patron club where you own the relationship with true fans.",
      "Limited-edition physical books, private workshops, or bespoke high-ticket consulting.",
      "Licensing your creative IP across different formats and mediums.",
    ],
    actionableFixes: [
      "Start an email newsletter immediately so an algorithm can't cut you off from fans.",
      "Turn every deep piece of work into 5 bite-sized snippets for different channels.",
      "Document your messy creative process in public; people love seeing behind the curtain.",
    ],
    probingQuestions: [
      "If your favorite social platform banned your account tomorrow, what survives?",
      "What is the distinct point of view that makes people immediately know it's you?",
      "Are you building a crowd of casual scrollers, or a group of genuine believers?",
    ],
    readingSources: [
      "Antifragile: Things That Gain from Disorder (Nassim Nicholas Taleb) — https://en.wikipedia.org/wiki/Antifragile_(book)",
      "Lateral Thinking: Creativity Step by Step (Edward de Bono) — https://en.wikipedia.org/wiki/Lateral_thinking",
      "Livewired & Incognito (David Eagleman) — https://davideagleman.com/books",
      "Stand Out of Our Light (James Williams) — https://en.wikipedia.org/wiki/Stand_Out_of_Our_Light",
      "The Righteous Mind (Jonathan Haidt) — https://righteousmind.com",
      "Show Your Work! (Austin Kleon) — https://austinkleon.com/show-your-work",
    ],
    ethics: "Loghy's Ethics Check: High signal, zero noise. Respect your audience's finite time on this planet.",
  },
};

const MARKETING_ETHICS =
  "Loghy's Ethics Check: Cut out fake countdown timers, fake scarcity, and sleazy marketing gimmicks. Deliver honest, undeniable value that respects people's intelligence.";

/** Mirrors the `domainLower.contains(...)` chain in evaluateSwot. */
function domainKey(domain) {
  const d = (domain || "").toLowerCase();
  if (d.includes("software") || d.includes("saas")) return "software";
  if (d.includes("hardware")) return "hardware";
  if (d.includes("food") || d.includes("local")) return "food";
  if (d.includes("service") || d.includes("professional")) return "services";
  return "creative";
}

export function evaluateSwot(concept, domain) {
  const key = domainKey(domain);
  const base = SWOT_BY_DOMAIN[key];
  const c = (concept || "").toLowerCase();
  const isMarketingFocus =
    (domain || "").toLowerCase().includes("market") ||
    ["market", "traffic", "growth", "ad", "lead"].some((w) => c.includes(w));

  return {
    domain,
    ...base,
    ethics: isMarketingFocus ? MARKETING_ETHICS : base.ethics,
  };
}

/* ------------------------------------------------------------- lateral tools */

const pick = (list) => list[Math.floor(Math.random() * list.length)];

export function generateRandomWordExercise(ideaTitle) {
  const word = pick(RandomStimulusWords);
  return {
    stimulus: word,
    prompt:
      `Lateral Stimulus: '${word}'.\n` +
      `Do not think of '${ideaTitle}'. For 30 seconds, write down 4 physical attributes of '${word}'. ` +
      `Then, force a connection: how does one of those attributes solve your toughest challenge?`,
  };
}

export function generateProvocationExercise() {
  const po = pick(ProvocationsPO);
  return {
    stimulus: po,
    prompt:
      `Provocation (${po}).\n` +
      `In lateral thinking, PO is a stepping stone. Even if it sounds absurd, do not dismiss it. ` +
      `If this rule were strictly enforced, what unexpected advantage or new business model appears?`,
  };
}

export function generateReversalExercise(ideaTitle) {
  const reversal = pick(Reversals);
  return {
    stimulus: reversal,
    prompt:
      `Problem Reversal:\n${reversal}\n` +
      `What hidden assumption does this reversal expose about '${ideaTitle}'?`,
  };
}

export function getHatExercise(index) {
  return SixHats[Math.min(Math.max(index, 0), SixHats.length - 1)];
}

/* ----------------------------------------------------------------- hit list */

export function calculateDevelopmentSchedule() {
  return [
    {
      phase: "Step 1: Ugly Prototype",
      title: "Build the ugly working prototype",
      description: "Don't touch colors or fonts yet. Just get the core engine working end-to-end to see if your concept makes sense.",
      estimatedHours: 3.5, effortLevel: "High", cognitiveLoad: "Deep Work", recommendedSprint: "Day 1",
    },
    {
      phase: "Step 2: Reality Probe",
      title: "Show it to 3 brutally honest people",
      description: "Watch their faces as they try it without you explaining. If they look confused, take notes and don't get defensive.",
      estimatedHours: 2.0, effortLevel: "Medium", cognitiveLoad: "Courage", recommendedSprint: "Day 2",
    },
    {
      phase: "Step 3: Feature Diet",
      title: "Cut the useless fluff & fix what broke",
      description: "Delete the buttons nobody clicked. Double down on the one thing that actually made someone say 'whoa'.",
      estimatedHours: 3.0, effortLevel: "High", cognitiveLoad: "Deep Work", recommendedSprint: "Day 3",
    },
    {
      phase: "Step 4: The Money Ask",
      title: "Ask for pre-orders or real commitments",
      description: "Polite compliments don't pay bills. See if anyone is willing to part with real cash or a signed signup.",
      estimatedHours: 2.0, effortLevel: "Critical", cognitiveLoad: "Sales Grit", recommendedSprint: "Day 4",
    },
    {
      phase: "Step 5: Ship It",
      title: "Push the simplest version out the door",
      description: "No more tweaking in the dark. Release it into the wild and let real user feedback guide the next step.",
      estimatedHours: 2.5, effortLevel: "Medium", cognitiveLoad: "Execution", recommendedSprint: "Day 5",
    },
  ];
}

/* --------------------------------------------------------- input assessment */

const RUDE_WORDS = [
  "stupid", "idiot", "dumb", "useless", "shut up", "moron", "trash", "garbage",
  "hate you", "worthless", "pathetic",
];

export function isExplicitlyRude(text) {
  const t = (text || "").toLowerCase();
  return RUDE_WORDS.some((w) => t.includes(w));
}

/**
 * Flags keyboard-mash input. Mirrors the app's heuristic: long runs of letters
 * with almost no vowels, or the same character hammered repeatedly.
 */
export function isUnclearOrGibberish(text) {
  const t = (text || "").trim().toLowerCase();
  if (t.length < 4) return false;
  if (/(.)\1{4,}/.test(t)) return true;

  const letters = t.replace(/[^a-z]/g, "");
  if (letters.length < 6) return false;
  const vowels = (letters.match(/[aeiou]/g) || []).length;
  const vowelRatio = vowels / letters.length;
  const hasSpaces = t.includes(" ");
  return vowelRatio < 0.18 || (!hasSpaces && letters.length > 18 && vowelRatio < 0.28);
}

export const randomThinkingQuote = () => pick(ThinkingQuotes);
export const randomInspiringThought = () => pick(InspiringThoughts);
export const randomRudeReprimand = () => pick(RudeReprimands);
export const randomGibberishReaction = () => pick(GibberishReactions);
export const randomPersonalityReaction = () => pick(PersonalityChangeReactions);

/* ------------------------------------------------- the Socratic chat answer */

/**
 * Builds the multi-channel reply the Stage screen shows: the four plain-language
 * SWOT angles, fixes, a probing question, a reading source and the ethics line.
 */
export function answerSocratically(userText, domain) {
  if (isExplicitlyRude(userText)) return randomRudeReprimand();
  if (isUnclearOrGibberish(userText)) return randomGibberishReaction();

  const s = evaluateSwot(userText, domain);
  const lines = [];
  lines.push("Let's test this from 4 practical angles:");
  lines.push("");
  lines.push(`• What holds up (Strengths): ${s.whatHoldsUp[0]}`);
  lines.push(`• Vulnerable spots (Weaknesses): ${s.vulnerableSpots[0]}`);
  lines.push(`• Outside hurdles (Threats): ${s.outsideHurdles[0]}`);
  lines.push(`• Hidden upside (Opportunities): ${s.hiddenUpside[0]}`);
  lines.push("");
  lines.push("How to fix the weak spots:");
  lines.push(`• ${s.actionableFixes[0]}`);
  lines.push(`• ${s.actionableFixes[1]}`);
  lines.push("");
  lines.push("Worth reading:");
  lines.push(`• ${s.readingSources[0]}`);
  lines.push("");
  lines.push("Question to test reality:");
  lines.push(pick(s.probingQuestions));
  lines.push("");
  lines.push(s.ethics);
  return lines.join("\n");
}
